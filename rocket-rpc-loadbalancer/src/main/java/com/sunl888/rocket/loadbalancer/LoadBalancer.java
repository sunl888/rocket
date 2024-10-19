package com.sunl888.rocket.loadbalancer;


import com.sunl888.rocket.common.annotation.SPI;
import com.sunl888.rocket.common.model.ServiceMeta;

import java.util.List;
import java.util.Map;

@SPI(RoundRobinLoadBalancer.NAME)
public interface LoadBalancer {

    /**
     * 选择服务节点
     *
     * @param params          参数
     * @param serviceMetaList 节点列表
     * @return serviceMeta
     */
    ServiceMeta select(Map<String, Object> params, List<ServiceMeta> serviceMetaList);
}
