package com.lvack.championggwrapper.retrofit.proxies;

import com.google.common.util.concurrent.RateLimiter;
import com.lvack.championggwrapper.annotations.Permits;
import com.lvack.championggwrapper.retrofit.APIResponse;
import com.lvack.championggwrapper.retrofit.Waitable;
import lombok.experimental.Delegate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class RateLimitingRetrofitProxy<T> implements InvocationHandler {
	private final T wrappedInstance;
	private final T instance;
	private final RateLimiter rateLimiter;

	private RateLimitingRetrofitProxy(RateLimiter rateLimiter, Class<T> service, T instanceToWrap) {
		this.wrappedInstance = instanceToWrap;
		this.rateLimiter = rateLimiter;
		//noinspection unchecked
		this.instance = (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service}, this);
	}

	public static <T> T wrap(RateLimiter rateLimiter, Class<T> service, T instanceToWrap) {
		return new RateLimitingRetrofitProxy<>(rateLimiter, service, instanceToWrap).instance;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (APIResponse.class.isAssignableFrom(method.getReturnType())) {
			return new FutureAPIResponse(method, args, getPermits(method));
		}
		return method.invoke(wrappedInstance, args);
	}

	private int getPermits(Method method) {
		if (method.isAnnotationPresent(Permits.class)) {
			Permits permits = method.getAnnotation(Permits.class);
			if (permits != null) return permits.value();
		}
		return 0;
	}

	private class FutureAPIResponse extends APIResponse {
		@Delegate(excludes = Waitable.class)
		private APIResponse response = new APIResponse();

		private FutureAPIResponse(Method method, Object[] args, int permits) {
			new Thread(() -> {
				if (0 < permits) rateLimiter.acquire(permits);
				//noinspection unchecked
				try {
					response = (APIResponse) method.invoke(wrappedInstance, args);
					new Thread(() -> {
						response.waitForResponse();
						notifyWaitingClients();
					}).start();
				} catch (IllegalAccessException | InvocationTargetException e) {
					response.setError(e);
					response.setState(State.FAILURE);
				}
			}).start();
		}
	}
}
