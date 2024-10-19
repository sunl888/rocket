package com.sunl888.rocket.loadbalancer;

import com.sunl888.rocket.common.annotation.Adaptive;
import com.sunl888.rocket.common.model.ServiceMeta;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Adaptive
public class RoundRobinLoadBalancer implements LoadBalancer {
    public static final String NAME = "roundRobin";
    private final AtomicInteger index = new AtomicInteger(0);

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
        return serviceMetaList.get(index.getAndIncrement() % serviceMetaList.size());
    }
}
