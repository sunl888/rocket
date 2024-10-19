package com.sunl888.rocket.core.proxy;

import com.sunl888.rocket.common.annotation.Adaptive;
import com.sunl888.rocket.core.proxy.interceptor.MethodInterceptor;

import java.lang.reflect.Proxy;

@Adaptive
@SuppressWarnings("all")
public class JdkServiceProxy implements ServiceProxy {
    public static final String NAME = "jdk";

    @Override
    public <T> T createProxy(Class<T> targetClass, MethodInterceptor interceptor) {
        return (T) Proxy.newProxyInstance(
                targetClass.getClassLoader(),
                targetClass.getInterfaces(),
                (proxy, method, args) -> {
                    return interceptor.intercept(proxy, method, args);
                }
        );
    }
}
