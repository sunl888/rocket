package com.sunl888.rocket.core.proxy;

import com.sunl888.rocket.common.annotation.SPI;
import com.sunl888.rocket.core.proxy.interceptor.MethodInterceptor;

@SPI(CglibServiceProxy.NAME)
public interface ServiceProxy {

    <T> T createProxy(Class<T> targetClass, MethodInterceptor interceptor);

}
