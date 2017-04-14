import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.error.AdvancedErrorResponse;
import com.lvack.championggwrapper.data.error.ErrorResponse;
import com.lvack.championggwrapper.data.error.NormalErrorResponse;
import com.lvack.championggwrapper.gson.EnumDeSerializer;
import com.lvack.championggwrapper.gson.ErrorResponseDeserializer;
import com.lvack.championggwrapper.gson.GsonProvider;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * DeSerializerTestClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

class DeSerializerTest {

	@Test
	void testErrorResponseDeserializer() {
		Gson gson = new GsonBuilder().registerTypeAdapter(ErrorResponse.class, new ErrorResponseDeserializer()).create();

		NormalErrorResponse normalErrorResponse = new NormalErrorResponse();
		normalErrorResponse.setError("error-content");
		normalErrorResponse.setMessage("message-content");
		normalErrorResponse.setStack("stack-content");

		AdvancedErrorResponse advancedErrorResponse = new AdvancedErrorResponse();
		advancedErrorResponse.setErrorResponse(normalErrorResponse);

		String failureJson = "[]";
		String successJsonNormal = "{\"error\":\"error-content\"," +
			"\"message\":\"message-content\",\"stack\":\"stack-content\"}";
		String successJsonAdvanced = "{\"error\":{\"error\":\"error-content\"," +
			"\"message\":\"message-content\",\"stack\":\"stack-content\"}}";

		Assertions.assertThrows(ErrorResponseDeserializer.BothAttemptsFailedException.class,
			() -> gson.fromJson(failureJson, ErrorResponse.class));


		Assert.assertEquals("the deserialized normal response is not equal to the expected one",
			normalErrorResponse, gson.fromJson(successJsonNormal, ErrorResponse.class));

		ErrorResponse actual = gson.fromJson(successJsonAdvanced, ErrorResponse.class);
		Assert.assertEquals("the deserialized advanced response is not equal to the expected one",
			advancedErrorResponse, actual);

		AdvancedErrorResponse actualAdvancedErrorResponse = (AdvancedErrorResponse) actual;

		Assert.assertEquals("actual advanced error response returned has an invalid error",
			normalErrorResponse.getError(), actualAdvancedErrorResponse.getError());

		Assert.assertEquals("actual advanced error response returned has an invalid message",
			normalErrorResponse.getMessage(), actualAdvancedErrorResponse.getMessage());

		Assert.assertEquals("actual advanced error response returned has an invalid stack",
			normalErrorResponse.getStack(), actualAdvancedErrorResponse.getStack());


	}

	@Test
	void testEnumDeSerializer() {
		Gson gson = new GsonBuilder()
			.registerTypeAdapter(TestEnum.class, new EnumDeSerializer<>(TestEnum.class)).create();

		TestEnum correctName = gson.fromJson(new JsonPrimitive("correctName"), TestEnum.class);
		Assert.assertEquals("deserialization of correctName did not return CORRECT_NAME",
			correctName, TestEnum.CORRECT_NAME);


		correctName = gson.fromJson(new JsonPrimitive("CORRECT_NAME"), TestEnum.class);
		Assert.assertEquals("deserialization of CORRECT_NAME did not return CORRECT_NAME",
			correctName, TestEnum.CORRECT_NAME);

		TestEnum validName = gson.fromJson(new JsonPrimitive("VALID_NAME"), TestEnum.class);
		Assert.assertEquals("deserialization of VALID_NAME did not return VALID_NAME",
			validName, TestEnum.VALID_NAME);

		TestEnum nullValue = gson.fromJson(JsonNull.INSTANCE, TestEnum.class);
		Assert.assertNull("deserialization of a JsonNull did not retrun null", nullValue);

		Assertions.assertThrows(JsonParseException.class,
			() -> gson.fromJson(new JsonPrimitive("invalid name"), TestEnum.class));

		Assertions.assertThrows(JsonParseException.class,
			() -> gson.fromJson(new JsonArray(), TestEnum.class));
	}

	@Test
	void testGsonProvider() {
		Assert.assertNotNull("gson provider did not provide a gson builder", GsonProvider.getGsonBuilder());
		Assert.assertNotNull("gson provider did not provide a gson instance", GsonProvider.getGson());
		Assert.assertEquals("gson provider did not provide the same gson instance twice",
			GsonProvider.getGson(), GsonProvider.getGson());

		Assertions.assertThrows(UnsupportedOperationException.class, GsonProvider::new);
	}

	private enum TestEnum {
		@SerializedName("correctName")CORRECT_NAME,
		VALID_NAME
	}
}
