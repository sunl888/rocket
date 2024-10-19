package com.sunl888.rocket.example;

import com.sunl888.rocket.common.config.ConfigManager;
import com.sunl888.rocket.core.Application;
import com.sunl888.rocket.core.factory.ServiceProxyFactory;
import com.sunl888.rocket.core.proxy.ServiceProxy;
import com.sunl888.rocket.core.proxy.interceptor.MethodInterceptorImpl;
import com.sunl888.rocket.example.service.EchoService;
import com.sunl888.rocket.example.service.HelloService;

public class Consumer {
    public static void main(String[] args) {
        // 生成配置
        genProviderConfig();

        // 核心逻辑启动
        Application.start();

        invokeHelloService();
        invokeEchoService();
    }

    static void genProviderConfig() {
        // 配置文件
        System.setProperty("rocket.registry.address", "127.0.0.1:2181");

//        System.setProperty("rocket.provider.host", "127.0.0.1");
//        System.setProperty("rocket.provider.port", "8080");
//        System.setProperty("rocket.provider.version", "prod-1.0.0");
    }

    static void invokeHelloService() {
        ConfigManager configManager = ConfigManager.getInstance();
        ServiceProxy serviceProxy = ServiceProxyFactory.getServiceProxy(configManager.getRocketConfig().getProxy());
        HelloService helloService = serviceProxy.createProxy(HelloService.class, new MethodInterceptorImpl());
        System.out.println(helloService.hello("sunl888"));
    }

    static void invokeEchoService() {
        ConfigManager configManager = ConfigManager.getInstance();
        ServiceProxy serviceProxy = ServiceProxyFactory.getServiceProxy(configManager.getRocketConfig().getProxy());
        EchoService echoService = serviceProxy.createProxy(EchoService.class, new MethodInterceptorImpl());
        echoService.echo("你好");
    }
}
