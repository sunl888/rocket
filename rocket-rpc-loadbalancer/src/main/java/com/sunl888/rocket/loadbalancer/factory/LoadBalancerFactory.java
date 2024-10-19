package com.sunl888.rocket.loadbalancer.factory;

import com.sunl888.rocket.loadbalancer.LoadBalancer;
import com.sunl888.rocket.spi.ExtensionLoader;

public class LoadBalancerFactory {
    public static LoadBalancer getInstance(String key) {
        return ExtensionLoader.getExtensionLoader(LoadBalancer.class).getExtension(key);
    }
}
