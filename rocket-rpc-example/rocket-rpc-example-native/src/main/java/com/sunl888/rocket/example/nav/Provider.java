package com.sunl888.rocket.example.nav;

import com.sunl888.rocket.common.config.ConfigManager;
import com.sunl888.rocket.common.config.ProviderConfig;
import com.sunl888.rocket.common.config.RocketConfig;
import com.sunl888.rocket.common.exception.ProviderException;
import com.sunl888.rocket.common.model.ServiceMeta;
import com.sunl888.rocket.common.util.Banner;
import com.sunl888.rocket.core.Application;
import com.sunl888.rocket.core.remoting.NettyServer;
import com.sunl888.rocket.core.remoting.Server;
import com.sunl888.rocket.example.common.service.EchoService;
import com.sunl888.rocket.example.common.service.HelloService;
import com.sunl888.rocket.example.common.service.impl.EchoServiceImpl;
import com.sunl888.rocket.example.common.service.impl.HelloServiceImpl;
import com.sunl888.rocket.registry.LocalRegistryCache;
import com.sunl888.rocket.registry.RegistryService;
import com.sunl888.rocket.registry.factory.RegistryServiceFactory;

public class Provider {
    public static void main(String[] args) {
        // 生成配置
        genProviderConfig();

        // 核心逻辑启动
        Application.start();

        // 注册服务
        registryService(EchoService.class, EchoServiceImpl.class);
        registryService(HelloService.class, HelloServiceImpl.class);

        // 暴露服务
        exportService();
    }

    static void genProviderConfig() {
        // 配置文件
        System.setProperty("rocket.registry.address", "127.0.0.1:2181");

        System.setProperty("rocket.provider.host", "127.0.0.1");
        System.setProperty("rocket.provider.port", "8080");
        System.setProperty("rocket.provider.version", "prod-1.0.0");
    }

    static void registryService(Class<?> service, Class<?> serviceImpl) {
        ConfigManager configManager = ConfigManager.getInstance();
        ProviderConfig providerConfig = configManager.getProviderConfig();
        RocketConfig rocketConfig = configManager.getRocketConfig();
        RegistryService registryService = RegistryServiceFactory.getInstance(rocketConfig.getRegistry());
        try {
            ServiceMeta serviceMeta = new ServiceMeta();
            serviceMeta.setServiceName(service.getName());
            serviceMeta.setServiceVersion(providerConfig.getVersion());
            serviceMeta.setServiceHost(providerConfig.getHost());
            serviceMeta.setServicePort(providerConfig.getPort());

            LocalRegistryCache.register(serviceMeta.getServiceName(), serviceImpl);
            registryService.register(serviceMeta);
        } catch (Exception e) {
            LocalRegistryCache.unregister(service.getName());
            throw new ProviderException(e);
        }
    }

    static void exportService() {
        ConfigManager configManager = ConfigManager.getInstance();
        ProviderConfig providerConfig = configManager.getProviderConfig();

        // 打印banner
        Banner.serverStartup(ConfigManager.getInstance().getProviderConfig().getPort());

        // 启动Server服务
        Server server = new NettyServer();
        server.start(providerConfig.getPort());
    }
}
