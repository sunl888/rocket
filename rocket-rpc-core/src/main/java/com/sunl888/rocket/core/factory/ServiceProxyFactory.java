package com.sunl888.rocket.core.factory;

import com.sunl888.rocket.core.proxy.ServiceProxy;
import com.sunl888.rocket.spi.ExtensionLoader;

public class ServiceProxyFactory {
    public static ServiceProxy getServiceProxy(String key) {
        return ExtensionLoader.getExtensionLoader(ServiceProxy.class).getExtension(key);
    }
}
