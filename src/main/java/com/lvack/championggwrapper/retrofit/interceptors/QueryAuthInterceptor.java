package com.lvack.championggwrapper.retrofit.interceptors;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


public class QueryAuthInterceptor implements Interceptor {
	private final String paramName;
	private final String apiKey;

	public QueryAuthInterceptor(String paramName, String apiKey) {
		this.paramName = paramName;
		this.apiKey = apiKey;
	}

	public Response intercept(Chain chain) throws IOException {
		Request orgRequest = chain.request();

		HttpUrl.Builder urlBuilder = orgRequest.url().newBuilder();
		urlBuilder.addQueryParameter(paramName, apiKey);

		Request newRequest = orgRequest.newBuilder().url(urlBuilder.build()).build();
		return chain.proceed(newRequest);
	}
}
