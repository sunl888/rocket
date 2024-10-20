package com.sunl888.rocket.spring.boot.starter;


import com.sunl888.rocket.common.config.ConfigManager;
import com.sunl888.rocket.common.exception.ProxyException;
import com.sunl888.rocket.core.factory.ServiceProxyFactory;
import com.sunl888.rocket.core.proxy.ServiceProxy;
import com.sunl888.rocket.core.proxy.interceptor.MethodInterceptorImpl;
import com.sunl888.rocket.spring.boot.starter.annotation.RocketReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

@Slf4j
public class ConsumerBootstrap implements BeanPostProcessor {
    private final ConfigManager configManager = ConfigManager.getInstance();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RocketReference annotation = declaredField.getAnnotation(RocketReference.class);
            if (annotation == null) {
                continue;
            }
            Class<?> interfaceClass = annotation.interfaceClass();
            if (interfaceClass == void.class) {
                interfaceClass = declaredField.getType();
            }
            declaredField.setAccessible(true);
            ServiceProxy serviceProxy = ServiceProxyFactory.getServiceProxy(configManager.getRocketConfig().getProxy());
            Object proxy = serviceProxy.createProxy(interfaceClass, new MethodInterceptorImpl());
            try {
                declaredField.set(bean, proxy);
                declaredField.setAccessible(false);
            } catch (IllegalAccessException e) {
                throw new ProxyException(e);
            }
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}
