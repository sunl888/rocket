package com.sunl888.rocket.core.proxy;

import com.sunl888.rocket.common.annotation.Adaptive;
import com.sunl888.rocket.common.exception.ProxyException;
import com.sunl888.rocket.core.proxy.interceptor.MethodInterceptor;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.Method;

@Adaptive
@SuppressWarnings("all")
public class JavassistServiceProxy implements ServiceProxy {
    public static final String NAME = "javassist";

    @Override
    public <T> T createProxy(Class<T> targetClass, MethodInterceptor interceptor) {
        try {
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setSuperclass(targetClass);
            Class<?> proxyClass = proxyFactory.createClass();

            T proxyInstance = (T) proxyClass.getDeclaredConstructor().newInstance();

            ((javassist.util.proxy.Proxy) proxyInstance).setHandler(new MethodHandler() {
                @Override
                public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                    return interceptor.intercept(self, thisMethod, args);
                }
            });

            return proxyInstance;
        } catch (Exception e) {
            throw new ProxyException("Javassist 动态代理创建失败", e);
        }
    }
}
