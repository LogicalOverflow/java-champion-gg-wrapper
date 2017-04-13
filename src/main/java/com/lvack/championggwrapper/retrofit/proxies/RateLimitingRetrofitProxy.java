package com.lvack.championggwrapper.retrofit.proxies;

import com.google.common.util.concurrent.RateLimiter;
import com.lvack.championggwrapper.annotations.Permits;

import java.lang.reflect.Method;

/**
 * RateLimitingRetrofitProxyClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

public class RateLimitingRetrofitProxy<T> extends AbstractRetrofitProxy<T> {
	private final RateLimiter rateLimiter;

	public static <T> T wrap(RateLimiter rateLimiter, Class<T> service, T instanceToWrap) {
		return new RateLimitingRetrofitProxy<>(rateLimiter, service, instanceToWrap).getInstance();
	}

	private RateLimitingRetrofitProxy(RateLimiter rateLimiter, Class<T> service, T instanceToWrap) {
		super(service, instanceToWrap);
		this.rateLimiter = rateLimiter;
	}

	@Override void preprocessMethod(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.isAnnotationPresent(Permits.class)) {
			Permits permits = method.getAnnotation(Permits.class);
			rateLimiter.acquire(permits.value());
		}
	}
}
