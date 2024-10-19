package com.sunl888.rocket.common.config;

import com.sunl888.rocket.common.enums.*;
import lombok.Data;

/**
 * 框架基础配置
 */
@Data
public class RocketConfig {
    private String loadBalancer = LoadBalancerEnum.RANDOM.getName();

    private String retryer = RetryEnum.NOT_RETRY.getName();

    private String cluster = ClusterEnum.FAIL_FAST.getName();

    private String compressor = CompressorEnum.GZIP.getName();

    private String serializer = SerializerEnum.FASTJSON.getName();

    private String invoker = InvokerEnum.Netty4.getName();

    private String proxy = ProxyEnum.CGLIB.getName();

    private String registry = RegistryEnum.ZOOKEEPER.getName();


}
