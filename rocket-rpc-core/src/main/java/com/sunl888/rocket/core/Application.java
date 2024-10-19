package com.sunl888.rocket.core;

import com.sunl888.rocket.common.config.ConfigManager;
import com.sunl888.rocket.common.config.RegistryConfig;
import com.sunl888.rocket.common.config.RocketConfig;
import com.sunl888.rocket.registry.RegistryService;
import com.sunl888.rocket.registry.factory.RegistryServiceFactory;

public class Application {

    public static void start() {
        ConfigManager configManager = ConfigManager.getInstance();
        RocketConfig rocketConfig = configManager.getRocketConfig();
        RegistryConfig registryConfig = configManager.getRegistryConfig();

        // 初始化注册中心（provider和consumer都需要做此操作）
        RegistryService registryService = RegistryServiceFactory.getInstance(rocketConfig.getRegistry());
        registryService.init(registryConfig);
    }
}
