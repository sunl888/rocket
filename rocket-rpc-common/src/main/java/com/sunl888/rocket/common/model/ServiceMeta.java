package com.sunl888.rocket.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ServiceMeta implements Serializable {
    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 服务版本
     */
    private String serviceVersion;

    /**
     * 服务地址
     */
    private String serviceHost;

    /**
     * 服务端口
     */
    private Integer servicePort;

}
