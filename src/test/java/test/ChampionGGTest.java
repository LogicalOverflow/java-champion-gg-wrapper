package test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.lvack.championggwrapper.ChampionGGAPIFactory;
import com.lvack.championggwrapper.annotations.*;
import com.lvack.championggwrapper.data.champion.HighLevelChampionData;
import com.lvack.championggwrapper.data.error.ErrorResponse;
import com.lvack.championggwrapper.data.staticdata.Role;
import com.lvack.championggwrapper.data.staticdata.RoleStatOrder;
import com.lvack.championggwrapper.data.staticdata.StatOrder;
import com.lvack.championggwrapper.retrofit.APIResponse;
import com.lvack.championggwrapper.retrofit.ChampionGGAPI;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.lang3.ClassUtils;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import util.Constants;
import util.MockDispatcher;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


@DisplayName("ChampionGGAPI tests") class ChampionGGTest {

	private static ChampionGGAPI API;
	private static MockWebServer webServer;
	private static MockDispatcher dispatcher;

	@BeforeAll
	static void initChampionGGAPI() {
		webServer = new MockWebServer();
		dispatcher = new MockDispatcher();
		webServer.setDispatcher(dispatcher);
		ChampionGGAPIFactory.BASE_URL = webServer.url("/").toString();

		ChampionGGAPIFactory championGGAPIFactory = new ChampionGGAPIFactory(Constants.API_KEY, -1);
		API = championGGAPIFactory.buildChampionGGAPI();
	}

	@AfterAll
	static void deleteChampionGGAPI() throws IOException {
		webServer.shutdown();
	}

	@TestFactory Iterable<DynamicTest> testAPI() {
		List<String> championKeys = Arrays.asList("Annie", "Bard", "Ekko", "Pantheon", "Poppy",
			"Tristana", "Tryndamere", "Zed", "NotFound");
		List<Integer> pages = Arrays.asList(1, 2, null);
		List<Integer> limits = Arrays.asList(5, 10, null);
		List<StatOrder> statOrders = Arrays.asList(StatOrder.values());
		List<RoleStatOrder> roleStatOrders = Arrays.asList(RoleStatOrder.values());
		List<Role> roles = Arrays.asList(Role.values());

		List<DynamicTest> tests = new ArrayList<>();

		for (Method method : ChampionGGAPI.class.getDeclaredMethods()) {
			Parameter[] parameters = method.getParameters();

			List<Object[]> args = new ArrayList<>();
			args.add(new Object[parameters.length]);
			args = extendWithValues(args, statOrders, parameters, OrderKey.class, StatOrder.class);
			args = extendWithValues(args, roleStatOrders, parameters, OrderKey.class, RoleStatOrder.class);
			args = extendWithValues(args, pages, parameters, PageNumber.class, Integer.class);
			args = extendWithValues(args, limits, parameters, PageLimit.class, Integer.class);
			args = extendWithValues(args, roles, parameters, RoleKey.class, Role.class);
			args = extendWithValues(args, championKeys, parameters, ChampionKey.class, String.class);

			args.stream().map(arg -> DynamicTest.dynamicTest(getMethodName(method, arg), () -> {
					APIResponse<?> actualResponse = (APIResponse) method.invoke(API, arg);
					actualResponse.waitForResponse();
				validateResponse(dispatcher.getLastResponse(), actualResponse, method);
				})
			).forEach(tests::add);
		}

		return tests;
	}

	@Test
	void testInvalidApiKey() {
		dispatcher.setDelay(0);
		ChampionGGAPI championGGAPI = new ChampionGGAPIFactory("invalid-key").buildChampionGGAPI();
		APIResponse<List<HighLevelChampionData>> response = championGGAPI.getHighLevelChampionData();
		response.waitForResponse();

		Assert.assertEquals("invalid api key response does not have state INVALID_API_KEY",
			APIResponse.State.INVALID_API_KEY, response.getState());
		Assert.assertTrue("valid api key with invalid api key", response.isInvalidAPIKey());

		Assert.assertNull("invalid api key response has content", response.getContent());
		Assert.assertNull("invalid api key response has an error response", response.getErrorResponse());
		Assert.assertNotNull("invalid api key response does not have an error exception", response.getError());
	}

	@Test
	void testRateLimiter() {
		dispatcher.setDelay(0);

		long requestDelay = 1000;
		double maxRequestsPerSecond = 1000.0 / requestDelay;
		ChampionGGAPIFactory factory = new ChampionGGAPIFactory(Constants.API_KEY, maxRequestsPerSecond);
		ChampionGGAPI api = factory.buildChampionGGAPI();

		final int calls = 4;

		List<APIResponse<List<HighLevelChampionData>>> responses = new ArrayList<>();
		Assertions.assertTimeout(Duration.ofMillis(100 * calls), () -> {
			for (int i = 0; i < calls; i++) responses.add(api.getHighLevelChampionData());
		}, "calling api " + calls + " times took too long");

		responses.forEach(APIResponse::waitForResponse);

		List<Long> callTimes = dispatcher.getCallTimes();
		List<Long> longs = callTimes.subList(callTimes.size() - calls, callTimes.size());
		List<Long> deltas = new ArrayList<>();

		for (int i = 0; i < longs.size() - 1; i++) deltas.add(longs.get(i + 1) - longs.get(i));

		for (int i = 0; i < deltas.size(); i++)
			Assert.assertTrue(String.format("the delay between call %d and %d was too low (%dms)",
				i, i + 1, deltas.get(i)), requestDelay * 0.8 <= deltas.get(i));

	}

	@Test
	void testSlowResponse() {
		dispatcher.setDelay(1000);

		final int calls = 4;

		List<APIResponse<List<HighLevelChampionData>>> responses = new ArrayList<>();
		Assertions.assertTimeout(Duration.ofMillis(100 * calls), () -> {
			for (int i = 0; i < calls; i++) responses.add(API.getHighLevelChampionData());
		}, "calling api " + calls + " times took too long");

		responses.forEach(APIResponse::waitForResponse);

		for (APIResponse<List<HighLevelChampionData>> response : responses) {
			Assert.assertTrue("one response is no success", response.isSuccess());
			Assert.assertNotNull("one response has no content", response.getContent());
		}

	}

	private void validateResponse(MockResponse response, APIResponse<?> actualResponse, Method method) throws Throwable {
		Assert.assertNotNull("mock response is null", response);

		Assert.assertTrue("is complete returned false after waitForResponse", actualResponse.isComplete());
		Assert.assertNotEquals("state is still IN_PROGRESS after waitForResponse",
			APIResponse.State.IN_PROGRESS, actualResponse.getState());

		Assert.assertFalse("got invalid api key, error stacktrace: "
			+ getStackTrace(actualResponse.getError()), actualResponse.isInvalidAPIKey());

		if (actualResponse.isFailure()) validateFailedRequest(actualResponse);

		String expectedResponseCode = response.getStatus().split(" ")[1];
		int actualResponseCode = actualResponse.getResponseCode();
		Assert.assertEquals("response codes does not match",
			expectedResponseCode, String.valueOf(actualResponseCode));

		if (actualResponse.isSuccess()) validateSuccessfulRequest(response, actualResponse, method);

		if (actualResponse.isAPIError()) validateAPIErrorResponse(response, actualResponse);

		if (actualResponse.isInvalidAPIKey()) {
			Assert.fail("invalid api key triggered with valid api key: " + actualResponse);
		}
	}

	private void validateFailedRequest(APIResponse<?> actualResponse) {
		Object actualContent = actualResponse.getContent();
		ErrorResponse actualErrorResponse = actualResponse.getErrorResponse();
		Throwable actualError = actualResponse.getError();

		Assert.assertTrue("isFailure returns true but state is not FAILURE",
			actualResponse.getState() == APIResponse.State.FAILURE);

		Assert.assertNull("failure response has content", actualContent);
		Assert.assertNull("failure response has an error response", actualErrorResponse);
		Assert.assertNotNull("failure response does not have an error exception", actualError);

		Assert.fail("an error occurred while executing an api request, stacktrace: "
			+ getStackTrace(actualError));
	}

	private void validateSuccessfulRequest(MockResponse response, APIResponse<?> actualResponse, Method method) throws UnsupportedEncodingException {
		Object actualContent = actualResponse.getContent();
		ErrorResponse actualErrorResponse = actualResponse.getErrorResponse();
		Throwable actualError = actualResponse.getError();

		Assert.assertTrue("isSuccess returns true but state is not SUCCESS",
			actualResponse.getState() == APIResponse.State.SUCCESS);
		Assert.assertNotNull("success response does not have content", actualContent);
		Assert.assertNull("success response has an error response", actualErrorResponse);
		Assert.assertNull("success response has an error exception", actualError);

		String expectedResponseJson = new String(response.getBody().clone().readByteArray(), "UTF-8");
		Object expectedContent = Constants.GSON.fromJson(expectedResponseJson,
			((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0]);
		Assert.assertEquals("response content does not match", expectedContent, actualContent);

		JsonElement expectedJsonElement = Constants.GSON.fromJson(expectedResponseJson, JsonElement.class);
		JsonElement actualJsonElement = Constants.GSON.fromJson(Constants.GSON.toJson(actualContent), JsonElement.class);
		assertEqualJsonElements(expectedJsonElement, actualJsonElement);
		// TODO: find fields that may be null; assertNoNullFields(actualContent);
	}

	private void validateAPIErrorResponse(MockResponse response, APIResponse<?> actualResponse) throws UnsupportedEncodingException {
		Object actualContent = actualResponse.getContent();
		ErrorResponse actualErrorResponse = actualResponse.getErrorResponse();
		Throwable actualError = actualResponse.getError();

		Assert.assertTrue("isAPIError returns true but state is not API_ERROR",
			actualResponse.getState() == APIResponse.State.API_ERROR);

		Assert.assertFalse("response code is 2xx but api error is set",
			200 <= actualResponse.getResponseCode()
				&& actualResponse.getResponseCode() < 300);

		Assert.assertNull("api error response has content", actualContent);
		Assert.assertNotNull("api error response does not have an error response", actualErrorResponse);
		Assert.assertNull("api error response has an error exception", actualError);

		ErrorResponse expectedErrorResponse = Constants.GSON.fromJson(new String(response.getBody().clone().readByteArray(), "UTF-8"),
			ErrorResponse.class);
		Assert.assertEquals("error response content does not match", expectedErrorResponse, actualErrorResponse);
	}

	private int getIndex(Parameter[] parameters, Class<? extends Annotation> targetAnnotation, Class<?> targetType) {
		Class<?> wrappedType = ClassUtils.primitiveToWrapper(targetType);
		for (int i = 0; i < parameters.length; i++) {
			if (wrappedType.isAssignableFrom(ClassUtils.primitiveToWrapper(parameters[i].getType()))
				&& parameters[i].isAnnotationPresent(targetAnnotation)) return i;

		}
		return -1;
	}

	private List<Object[]> extendWithValues(List<Object[]> args, int index, List<?> values) {
		if (index < 0) return args;

		List<Object[]> extended = new ArrayList<>();
		for (Object[] arg : args) {
			for (Object value : values) {
				Object[] clone = arg.clone();
				clone[index] = value;
				extended.add(clone);
			}
		}
		return extended;
	}

	private <T> List<Object[]> extendWithValues(List<Object[]> args, List<T> values, Parameter[] parameters, Class<? extends Annotation> targetAnnotation, Class<T> targetType) {
		return extendWithValues(args, getIndex(parameters, targetAnnotation, targetType), values);
	}

	private String getStackTrace(Throwable t) {
		if (t == null) return "throwable is null";
		StringWriter stringWriter = new StringWriter();
		t.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	private void assertEqualJsonElements(JsonElement expected, JsonElement actual) {
		assertEqualJsonElements(expected, actual, "");
	}

	private void assertEqualJsonElements(JsonElement expected, JsonElement actual, String path) {
		if (expected == null) {
			Assert.assertNull("expected is null actual not", actual);
			return;
		}

		Assert.assertEquals("wrong element type at " + path, getJsonType(expected), getJsonType(actual));
		if (expected.isJsonNull()) return;

		if (expected.isJsonArray()) assertEqualsJsonArray(expected.getAsJsonArray(), actual.getAsJsonArray(), path);

		if (expected.isJsonObject()) assertEqualsJsonObject(expected.getAsJsonObject(), actual.getAsJsonObject(), path);

		if (expected.isJsonPrimitive())
			assertEqualsJsonPrimitive(expected.getAsJsonPrimitive(), actual.getAsJsonPrimitive(), path);
	}

	private void assertEqualsJsonArray(JsonArray expectedArray, JsonArray actualArray, String path) {
		Assert.assertEquals("json array at " + path + " has wrong length",
			expectedArray.size(), actualArray.size());

		for (int i = 0; i < expectedArray.size(); i++)
			assertEqualJsonElements(expectedArray.get(i), actualArray.get(i), path + "[" + i + "]");
	}

	private void assertEqualsJsonObject(JsonObject expectedObject, JsonObject actualObject, String path) {
		for (Map.Entry<String, JsonElement> expectedEntry : expectedObject.entrySet()) {
			String expectedKey = expectedEntry.getKey();
			JsonElement expectedValue = expectedEntry.getValue();

			String entryPath = path + "[" + expectedKey + "]";


			JsonElement actualValue = actualObject.get(expectedKey);
			if (expectedValue == null) continue;
			Assert.assertNotNull("missing key at path " + entryPath +
				" of type " + getJsonType(expectedValue) + ", expected value " +
				expectedValue + ", enclosing object: " + expectedObject, actualValue);

			assertEqualJsonElements(expectedValue, actualValue, entryPath);
		}

		for (Map.Entry<String, JsonElement> actualEntry : actualObject.entrySet()) {
			JsonElement actualValue = actualEntry.getValue();
			if (actualValue == null) continue;
			String actualKey = actualEntry.getKey();
			JsonElement expectedValue = expectedObject.get(actualKey);

			Assert.assertNotNull("unknown key " + path + "[" + actualKey + "] of type " +
				getJsonType(actualValue) + " with value " +
				actualValue + " present, enclosing object: " + actualObject, expectedValue);
		}
	}

	private void assertEqualsJsonPrimitive(JsonPrimitive expectedPrimitive, JsonPrimitive actualPrimitive, String path) {
		if (expectedPrimitive.isString()) {
			Assert.assertEquals("strings at " + path + " do not match",
				expectedPrimitive.getAsString(), actualPrimitive.getAsString());
		}
		if (expectedPrimitive.isBoolean()) {
			Assert.assertEquals("booleans at " + path + " do not match",
				expectedPrimitive.getAsBoolean(), actualPrimitive.getAsBoolean());
		}
		if (expectedPrimitive.isNumber()) {
			boolean equal = expectedPrimitive.getAsBigDecimal().compareTo(actualPrimitive.getAsBigDecimal()) == 0;
			Assert.assertTrue("numbers at " + path + " do not match expected <" +
					expectedPrimitive.getAsBigDecimal() + "> but was:<" +
					actualPrimitive.getAsBigDecimal() + ">",
				equal);
		}
		Assert.assertEquals("expected primitive (" + expectedPrimitive + ") and actual primitive (" +
			actualPrimitive + ") at " + path + " are not equal; ", expectedPrimitive, actualPrimitive);
	}

	@Test
	void testObjectMethodOnApi() {
		//noinspection ResultOfMethodCallIgnored
		new ChampionGGAPIFactory(Constants.API_KEY, 10)
			.buildChampionGGAPI().getClass();
	}

	private String getJsonType(JsonElement element) {
		if (element == null) return "null";
		if (element.isJsonArray()) return "json array";
		if (element.isJsonPrimitive()) return "json primitive " + getJsonPrimitiveType(element.getAsJsonPrimitive());
		if (element.isJsonNull()) return "json null";
		if (element.isJsonObject()) return "json object";
		return "unknown";
	}

	private String getJsonPrimitiveType(JsonPrimitive primitive) {
		if (primitive.isString()) return "string";
		if (primitive.isBoolean()) return "boolean";
		if (primitive.isNumber()) return "number";
		return "unknown";
	}

	/* private void assertNoNullFields(Object object) {
		assertNoNullFields(object, "");
	} */

	/* private void assertNoNullFields(Object object, String path) {
		Assert.assertNotNull("field at " + path + " is null", object);
		if (ClassUtils.isPrimitiveOrWrapper(object.getClass())) return;
		if (object instanceof String) return;

		if (object.getClass().isArray()) {
			Object[] array = (Object[]) object;
			for (int i = 0; i < array.length; i++) assertNoNullFields(array[i], path + "[" + i + "]");
		} else if (Iterable.class.isAssignableFrom(object.getClass())) {
			Iterable iterable = (Iterable) object;
			int i = 0;
			for (Object element : iterable) {
				assertNoNullFields(element, path + "[" + i + "]");
				i++;
			}
		} else if (!(object instanceof Enum)) {
			for (Field field : object.getClass().getDeclaredFields()) {
				String fieldPath = path + "[" + field.getName() + "]";
				if (ClassUtils.isPrimitiveOrWrapper(field.getType())) continue;
				if (field.getDeclaringClass() == Object.class) continue;
				try {
					if (!field.isAccessible()) field.setAccessible(true);
					Object value = field.get(object);
					assertNoNullFields(value, fieldPath);
				} catch (IllegalAccessException e) {
					Assert.fail("failed to access field at " + fieldPath + ", stacktrace: " + getStackTrace(e));
				}
			}
		}
	} */

	private String getMethodName(Method method, Object[] args) {
		return method.getDeclaringClass().getSimpleName() + "." + method.getName() +
			Arrays.stream(args).map(Objects::toString).collect(Collectors.joining(";", "(", ")"));
	}

}
