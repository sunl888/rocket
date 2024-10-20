package com.sunl888.rocket.example;

import com.sunl888.rocket.common.config.ConfigManager;
import com.sunl888.rocket.core.Application;
import com.sunl888.rocket.core.factory.ServiceProxyFactory;
import com.sunl888.rocket.core.proxy.ServiceProxy;
import com.sunl888.rocket.core.proxy.interceptor.MethodInterceptorImpl;
import com.sunl888.rocket.example.service.EchoService;
import com.sunl888.rocket.example.service.HelloService;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Consumer {
    public static void main(String[] args) throws InterruptedException {
        // 生成配置
        genProviderConfig();

        // 核心逻辑启动
        Application.start();

        ThreadPoolExecutor pool = new ThreadPoolExecutor(8, 20,
                60, TimeUnit.MINUTES, new LinkedBlockingDeque<>());

        for (int i = 0; i < 500; i++) {
            int finalI = i;
            pool.execute(() -> invokeHelloService(finalI));
            pool.execute(() -> invokeEchoService(finalI));
        }

        TimeUnit.SECONDS.sleep(60);

        for (int i = 1000; i < 5000; i++) {
            int finalI = i;
            pool.execute(() -> invokeHelloService(finalI));
            pool.execute(() -> invokeEchoService(finalI));
        }

        pool.shutdown();

        while (!pool.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("OK");
    }

    static void genProviderConfig() {
        // 配置文件
        System.setProperty("rocket.registry.address", "127.0.0.1:2181");
        System.setProperty("rocket.provider.version", "prod-1.0.0");
    }

    static void invokeHelloService(int i) {
        ConfigManager configManager = ConfigManager.getInstance();
        ServiceProxy serviceProxy = ServiceProxyFactory.getServiceProxy(configManager.getRocketConfig().getProxy());
        HelloService helloService = serviceProxy.createProxy(HelloService.class, new MethodInterceptorImpl());
        System.out.println(helloService.hello("sunl888 " + i));
    }

    static void invokeEchoService(int i) {
        ConfigManager configManager = ConfigManager.getInstance();
        ServiceProxy serviceProxy = ServiceProxyFactory.getServiceProxy(configManager.getRocketConfig().getProxy());
        EchoService echoService = serviceProxy.createProxy(EchoService.class, new MethodInterceptorImpl());
        System.out.println(echoService.echo("你好 " + i));
    }
}
