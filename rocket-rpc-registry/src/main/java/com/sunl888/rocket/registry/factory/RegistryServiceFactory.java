package com.sunl888.rocket.registry.factory;

import com.sunl888.rocket.registry.RegistryService;
import com.sunl888.rocket.spi.ExtensionLoader;

public class RegistryServiceFactory {
    public static RegistryService getInstance(String key) {
        return ExtensionLoader.getExtensionLoader(RegistryService.class).getExtension(key);
    }
}
