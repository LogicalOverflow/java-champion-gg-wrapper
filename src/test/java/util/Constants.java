package util;

import com.google.gson.Gson;
import com.lvack.championggwrapper.gson.GsonProvider;
import okhttp3.mockwebserver.MockResponse;

/**
 * ConstantsClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

public class Constants {
	public static final Gson GSON = GsonProvider.getGsonBuilder()
		.registerTypeAdapter(MockResponse.class, new MockResponseDeSerializer())
		.create();
	public static final String API_KEY = "mock-api-key";
}
