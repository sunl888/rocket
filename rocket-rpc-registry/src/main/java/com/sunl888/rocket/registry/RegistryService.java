package com.sunl888.rocket.registry;

import com.sunl888.rocket.common.annotation.SPI;
import com.sunl888.rocket.common.config.RegistryConfig;
import com.sunl888.rocket.common.model.ServiceMeta;

import java.util.List;

@SPI(ZookeeperRegistryService.NAME)
public interface RegistryService {
    default void init(RegistryConfig config) {
    }

    void register(ServiceMeta serviceMeta) throws Exception;

    void unregister(ServiceMeta serviceMeta);

    List<ServiceMeta> serviceDiscovery(String serviceName);

    default void destroy() {
        // 销毁
    }

    default void healthCheck() {
        // 健康检查
    }

    default void watch(ServiceMeta serviceMeta) {
        // 监听服务改变
    }
}
