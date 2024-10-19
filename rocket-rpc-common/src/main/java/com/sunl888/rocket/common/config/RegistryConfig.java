package com.sunl888.rocket.common.config;

import lombok.Data;

/**
 * 注册中心配置
 */
@Data
public class RegistryConfig {
    /**
     * 地址
     */
    private String address;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间
     */
    private Long timeout = 100000L;

    /**
     * 重试次数
     */
    private Integer retryCount = 3;
}
