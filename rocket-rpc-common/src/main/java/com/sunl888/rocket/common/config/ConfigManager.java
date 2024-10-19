package com.sunl888.rocket.common.config;

import lombok.Getter;
import org.dromara.hutool.setting.props.Props;
import org.dromara.hutool.setting.props.PropsUtil;

import static com.sunl888.rocket.common.constants.ApplicationConstant.ROCKET;

@Getter
public class ConfigManager {
    private static ConfigManager instance;
    private RocketConfig rocketConfig;
    private ConsumerConfig consumerConfig;
    private ProviderConfig providerConfig;
    private RegistryConfig registryConfig;

    private ConfigManager() {
        loadConfig();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    void loadConfig() {
        Props systemProps = PropsUtil.getSystemProps();

        rocketConfig = systemProps.toBean(RocketConfig.class, ROCKET);
        consumerConfig = systemProps.toBean(ConsumerConfig.class, ROCKET + ".consumer");
        providerConfig = systemProps.toBean(ProviderConfig.class, ROCKET + ".provider");
        registryConfig = systemProps.toBean(RegistryConfig.class, ROCKET + ".registry");
    }
}
