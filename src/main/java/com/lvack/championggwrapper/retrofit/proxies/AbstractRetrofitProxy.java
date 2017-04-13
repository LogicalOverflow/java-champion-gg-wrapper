package com.lvack.championggwrapper.retrofit.proxies;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


abstract class AbstractRetrofitProxy<T> {
	private final T wrappedInstance;
	private final T instance;

	AbstractRetrofitProxy(Class<T> service, T instanceToWrap) {
		this.wrappedInstance = instanceToWrap;
		//noinspection unchecked
		this.instance = (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
				this::invoke);
	}

	public T getInstance() {
		return instance;
	}

	void preprocessMethod(Object proxy, Method method, Object[] args) throws Throwable {}

	void postprocessMethod(Object proxy, Method method, Object[] args, Object result) throws Throwable {}

	private Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.getDeclaringClass() == Object.class) {
			return method.invoke(this, args);
		}

		preprocessMethod(proxy, method, args);
		Object result = method.invoke(wrappedInstance, args);
		postprocessMethod(proxy, method, args, result);
		return result;
	}
}
