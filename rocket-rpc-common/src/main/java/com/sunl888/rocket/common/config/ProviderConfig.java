package com.sunl888.rocket.common.config;

import lombok.Data;

/**
 * 提供者配置
 */
@Data
public class ProviderConfig {
    private String name;

    private String version;

    private String group;

    private String host;

    private int port;

    private int threads = Runtime.getRuntime().availableProcessors() << 2;
    private int nThreads = 1;

    private Integer alive;

    private Integer queues;

    private Integer payload;

}
