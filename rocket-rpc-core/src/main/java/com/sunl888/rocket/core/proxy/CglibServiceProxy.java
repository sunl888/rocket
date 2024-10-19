package com.sunl888.rocket.core.proxy;

import com.sunl888.rocket.common.annotation.Adaptive;
import com.sunl888.rocket.common.exception.ProxyException;
import com.sunl888.rocket.core.proxy.interceptor.MethodInterceptor;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Adaptive
@SuppressWarnings("all")
public class CglibServiceProxy implements ServiceProxy {
    public static final String NAME = "cglib";

    @Override
    public <T> T createProxy(Class<T> targetClass, MethodInterceptor interceptor) {
        try {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(targetClass);
            enhancer.setCallback(new net.sf.cglib.proxy.MethodInterceptor() {
                @Override
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                    return interceptor.intercept(obj, method, args);
                }
            });
            return (T) enhancer.create();
        } catch (Exception e) {
            throw new ProxyException("Cglib 动态代理创建失败", e);
        }
    }
}
