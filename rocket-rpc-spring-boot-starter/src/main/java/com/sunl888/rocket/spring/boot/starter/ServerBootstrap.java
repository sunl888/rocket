package com.sunl888.rocket.spring.boot.starter;

import com.sunl888.rocket.common.config.ConfigManager;
import com.sunl888.rocket.common.config.ProviderConfig;
import com.sunl888.rocket.common.util.Banner;
import com.sunl888.rocket.core.Application;
import com.sunl888.rocket.core.remoting.NettyServer;
import com.sunl888.rocket.core.remoting.Server;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class ServerBootstrap implements ImportBeanDefinitionRegistrar {
    static void exportService() {
        ConfigManager configManager = ConfigManager.getInstance();
        ProviderConfig providerConfig = configManager.getProviderConfig();

        // 打印banner
        Banner.serverStartup(ConfigManager.getInstance().getProviderConfig().getPort());

        // 启动Server服务
        Server server = new NettyServer();
        server.start(providerConfig.getPort());
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 注册中心启动
        Application.start();

        // 用新的线程启动服务端
        new Thread(ServerBootstrap::exportService).start();
    }
}
