package com.sunl888.rocket.spring.boot.starter;

import com.sunl888.rocket.common.config.ConfigManager;
import com.sunl888.rocket.common.config.ProviderConfig;
import com.sunl888.rocket.common.config.RocketConfig;
import com.sunl888.rocket.common.exception.ProviderException;
import com.sunl888.rocket.common.model.ServiceMeta;
import com.sunl888.rocket.registry.LocalRegistryCache;
import com.sunl888.rocket.registry.RegistryService;
import com.sunl888.rocket.registry.factory.RegistryServiceFactory;
import com.sunl888.rocket.spring.boot.starter.annotation.RocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class ProviderBootstrap implements BeanPostProcessor {
    private final ConfigManager configManager = ConfigManager.getInstance();

    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Class<?> clazz = bean.getClass();
        RocketService annotation = clazz.getAnnotation(RocketService.class);
        if (annotation != null) {
            // 注册服务
            registryService(bean.getClass(), bean.getClass());
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    void registryService(Class<?> service, Class<?> serviceImpl) {
        ProviderConfig providerConfig = configManager.getProviderConfig();
        RocketConfig rocketConfig = configManager.getRocketConfig();
        RegistryService registryService = RegistryServiceFactory.getInstance(rocketConfig.getRegistry());
        try {
            ServiceMeta serviceMeta = new ServiceMeta();
            serviceMeta.setServiceName(service.getName());
            serviceMeta.setServiceVersion(providerConfig.getVersion());
            serviceMeta.setServiceHost(providerConfig.getHost());
            serviceMeta.setServicePort(providerConfig.getPort());

            LocalRegistryCache.register(serviceMeta.getServiceName(), serviceImpl);
            registryService.register(serviceMeta);
        } catch (Exception e) {
            LocalRegistryCache.unregister(service.getName());
            throw new ProviderException(e);
        }
    }
}
