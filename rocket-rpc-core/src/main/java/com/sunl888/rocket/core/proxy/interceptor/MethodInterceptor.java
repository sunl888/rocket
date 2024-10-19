package com.sunl888.rocket.core.proxy.interceptor;

import java.lang.reflect.Method;

public interface MethodInterceptor {

    Object intercept(Object obj, Method method, Object[] args) throws Throwable;

}
