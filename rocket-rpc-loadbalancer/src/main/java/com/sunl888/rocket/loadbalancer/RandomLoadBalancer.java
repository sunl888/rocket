package com.sunl888.rocket.loadbalancer;

import com.sunl888.rocket.common.annotation.Adaptive;
import com.sunl888.rocket.common.model.ServiceMeta;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Adaptive
public class RandomLoadBalancer implements LoadBalancer {
    public static final String NAME = "random";
    private final Random random = new SecureRandom();

    /**
     * 选择服务节点
     *
     * @param params          参数
     * @param serviceMetaList 节点列表
     * @return serviceMeta
     */
    @Override
    public ServiceMeta select(Map<String, Object> params, List<ServiceMeta> serviceMetaList) {
        if (serviceMetaList == null || serviceMetaList.isEmpty()) {
            return null;
        }
        if (serviceMetaList.size() > 1) {
            return serviceMetaList.get(random.nextInt(serviceMetaList.size()));
        }
        return serviceMetaList.getFirst();
    }
}
