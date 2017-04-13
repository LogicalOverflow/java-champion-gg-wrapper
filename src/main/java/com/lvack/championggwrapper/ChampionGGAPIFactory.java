package com.lvack.championggwrapper;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.util.concurrent.RateLimiter;
import com.lvack.championggwrapper.gson.GsonProvider;
import com.lvack.championggwrapper.retrofit.APIResponseAdapterFactory;
import com.lvack.championggwrapper.retrofit.ChampionGGAPI;
import com.lvack.championggwrapper.retrofit.interceptors.QueryAuthInterceptor;
import com.lvack.championggwrapper.retrofit.proxies.RateLimitingRetrofitProxy;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ChampionGGClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

public class ChampionGGAPIFactory {
	@VisibleForTesting
	public static String BASE_URL = "http://api.champion.gg/";
	private final String apiKey;
	private final RateLimiter rateLimiter;

	public ChampionGGAPIFactory(String apiKey) {
		this(apiKey, 0);
	}

	public ChampionGGAPIFactory(String apiKey, double maxRequestsPerSecond) {
		this.apiKey = apiKey;
		if (0 < maxRequestsPerSecond) this.rateLimiter = RateLimiter.create(maxRequestsPerSecond);
		else this.rateLimiter = null;
	}

	public ChampionGGAPI buildChampionGGAPI() {
		OkHttpClient client = new OkHttpClient.Builder()
				.addInterceptor(new QueryAuthInterceptor("api_key", apiKey)).build();

		Retrofit retrofit = new Retrofit.Builder()
				.client(client)
				.baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create(GsonProvider.getGson()))
				.addCallAdapterFactory(APIResponseAdapterFactory.create())
				.build();

		ChampionGGAPI instance = retrofit.create(ChampionGGAPI.class);
		if (rateLimiter == null) return instance;
		return RateLimitingRetrofitProxy.wrap(rateLimiter, ChampionGGAPI.class, instance);
	}
}
