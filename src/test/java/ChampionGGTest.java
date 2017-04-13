import com.google.gson.*;
import com.lvack.championggwrapper.ChampionGGAPIFactory;
import com.lvack.championggwrapper.annotations.*;
import com.lvack.championggwrapper.data.error.ErrorResponse;
import com.lvack.championggwrapper.data.champion.HighLevelChampionData;
import com.lvack.championggwrapper.data.staticdata.Role;
import com.lvack.championggwrapper.data.staticdata.RoleStat;
import com.lvack.championggwrapper.data.staticdata.RoleStatOrder;
import com.lvack.championggwrapper.data.staticdata.StatOrder;
import com.lvack.championggwrapper.gson.GsonProvider;
import com.lvack.championggwrapper.retrofit.APIResponse;
import com.lvack.championggwrapper.retrofit.ChampionGGAPI;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.lang3.ClassUtils;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MainClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */


@DisplayName("ChampionGGAPI tests") class ChampionGGTest {
	static final Gson GSON = GsonProvider.getGsonBuilder()
			.registerTypeAdapter(MockResponse.class, new MockResponseDeSerializer())
			.create();
	private static ChampionGGAPI API;
	private static MockWebServer webServer;
	private static MockDispatcher dispatcher;

	@BeforeAll
	static void initChampionGGAPI() {
		webServer = new MockWebServer();
		dispatcher = new MockDispatcher();
		webServer.setDispatcher(dispatcher);
		ChampionGGAPIFactory.BASE_URL = webServer.url("/").toString();

		ChampionGGAPIFactory championGGAPIFactory = new ChampionGGAPIFactory("mock-api-key", -1);
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
						MockResponse response = dispatcher.getLastResponse();
						Assert.assertNotNull("mock response is null", response);

						Assert.assertTrue("is complete returned false after waitForResponse", actualResponse.isComplete());
						Assert.assertNotEquals("state is still IN_PROGRESS after waitForResponse",
								APIResponse.State.IN_PROGRESS, actualResponse.getState());

						if (actualResponse.isFailure()) {
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

						String expectedResponseCode = response.getStatus().split(" ")[1];
						int actualResponseCode = actualResponse.getResponseCode();
						Assert.assertEquals("response codes does not match",
								expectedResponseCode, String.valueOf(actualResponseCode));

						if (actualResponse.isSuccess()) {
							Object actualContent = actualResponse.getContent();
							ErrorResponse actualErrorResponse = actualResponse.getErrorResponse();
							Throwable actualError = actualResponse.getError();

							Assert.assertTrue("isSuccess returns true but state is not SUCCESS",
									actualResponse.getState() == APIResponse.State.SUCCESS);
							Assert.assertNotNull("success response does not have content", actualContent);
							Assert.assertNull("success response has an error response", actualErrorResponse);
							Assert.assertNull("success response has an error exception", actualError);

							String expectedResponseJson = new String(response.getBody().clone().readByteArray(), "UTF-8");
							Object expectedContent = GSON.fromJson(expectedResponseJson,
									((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0]);
							Assert.assertEquals("response content does not match", expectedContent, actualContent);

							JsonElement expectedJsonElement = GSON.fromJson(expectedResponseJson, JsonElement.class);
							JsonElement actualJsonElement = GSON.fromJson(GSON.toJson(actualContent), JsonElement.class);
							assertEqualJsonElements(expectedJsonElement, actualJsonElement);
							// TODO: find fields that may be null; assertNoNullFields(actualContent);
						}

						if (actualResponse.isAPIError()) {
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

							ErrorResponse expectedErrorResponse = GSON.fromJson(new String(response.getBody().clone().readByteArray(), "UTF-8"),
									ErrorResponse.class);
							Assert.assertEquals("error response content does not match", expectedErrorResponse, actualErrorResponse);
						}

						if (actualResponse.isInvalidAPIKey()) {
							Assert.fail("invalid api key triggered with valid api key: " + actualResponse);
						}
					})
			).forEach(tests::add);
		}

		return tests;
	}

	private int getIndex(Parameter[] parameters, Class<? extends Annotation> targetAnnotation, Class<?> targetType) {
		targetType = ClassUtils.primitiveToWrapper(targetType)
		;
		for (int i = 0; i < parameters.length; i++) {
			if (targetType.isAssignableFrom(ClassUtils.primitiveToWrapper(parameters[i].getType()))
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

	@Test
	void testInvalidApiKey() {
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

	private String getStackTrace(Throwable t) {
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
		}

		Assert.assertEquals("wrong element type at " + path, getJsonType(expected), getJsonType(actual));
		if (expected.isJsonNull()) return;
		if (expected.isJsonArray()) {
			JsonArray expectedArray = expected.getAsJsonArray();
			JsonArray actualArray = actual.getAsJsonArray();

			Assert.assertEquals("json array at " + path + " has wrong length",
					expectedArray.size(), actualArray.size());

			for (int i = 0; i < expectedArray.size(); i++)
				assertEqualJsonElements(expectedArray.get(i), actualArray.get(i), path + "[" + i + "]");
		}

		if (expected.isJsonObject()) {
			JsonObject expectedObject = expected.getAsJsonObject();
			JsonObject actualObject = actual.getAsJsonObject();

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

		if (expected.isJsonPrimitive()) {
			JsonPrimitive expectedPrimitive = expected.getAsJsonPrimitive();
			JsonPrimitive actualPrimitive = actual.getAsJsonPrimitive();

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
		}
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

	private void assertNoNullFields(Object object) {
		assertNoNullFields(object, "");
	}

	private void assertNoNullFields(Object object, String path) {
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
	}

	private String getMethodName(Method method, Object[] args) {
		return method.getDeclaringClass().getSimpleName() + "." + method.getName() +
				Arrays.stream(args).map(Objects::toString).collect(Collectors.joining(";", "(", ")"));
	}

}
