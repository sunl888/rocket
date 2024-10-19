package com.sunl888.rocket.common.util;

import com.sunl888.rocket.common.model.ServiceMeta;
import org.dromara.hutool.core.text.StrUtil;

public class ServiceMetaUtil {
    public static String buildServiceKey(String serviceName, String serviceVersion) {
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    public static String buildServiceKey(ServiceMeta serviceMeta) {
        return String.format("%s:%s", serviceMeta.getServiceName(), serviceMeta.getServiceVersion());
    }

    public static String buildServiceNodeKey(ServiceMeta serviceMeta) {
        return String.format("%s/%s", buildServiceKey(serviceMeta), buildServiceAddress(serviceMeta));
    }

    /**
     * 构建服务地址
     * <p>
     * 例如：https://127.0.0.1:8080
     *
     * @param serviceMeta 服务信息
     * @return 服务地址
     */
    public static String buildServiceAddress(ServiceMeta serviceMeta) {
        if (!StrUtil.contains(serviceMeta.getServiceHost(), "http")) {
            return String.format("https://%s:%s", serviceMeta.getServiceHost(), serviceMeta.getServicePort());
        }
        return String.format("%s:%s", serviceMeta.getServiceHost(), serviceMeta.getServicePort());
    }

    /**
     * 构建简单服务地址
     * <p>
     * 例如：127.0.0.1:8080
     *
     * @param serviceMeta 服务信息
     * @return 服务地址
     */
    public static String buildSimpleServiceAddress(ServiceMeta serviceMeta) {
        return String.format("%s:%s", serviceMeta.getServiceHost(), serviceMeta.getServicePort());
    }

}
