import com.google.gson.*;
import okhttp3.mockwebserver.MockResponse;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * MockResponseDeSerializerClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

public class MockResponseDeSerializer implements JsonDeserializer<MockResponse>, JsonSerializer<MockResponse>, InstanceCreator<MockResponse> {
	@Override
	public MockResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		MockResponse instance = createInstance(null);

		instance.setBody(jsonObject.get("body").getAsString());
		instance.setStatus(jsonObject.get("status").getAsString());
		return instance;
	}

	@Override public JsonElement serialize(MockResponse src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		try {
			jsonObject.add("body",
					new JsonPrimitive(new String(src.getBody().clone().readByteArray(), "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new JsonParseException("failed to encode body with UTF-8", e);
		}

		jsonObject.add("status", new JsonPrimitive(src.getStatus()));

		return jsonObject;
	}

	@Override public MockResponse createInstance(Type type) {
		MockResponse response = new MockResponse();
		response.setBodyDelay(0, TimeUnit.MILLISECONDS);
		return response;
	}
}
