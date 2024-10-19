package com.sunl888.rocket.core.factory;

import com.sunl888.rocket.core.tolerant.Tolerant;
import com.sunl888.rocket.spi.ExtensionLoader;

public class TolerantFactory {
    public static Tolerant getInstance(String key) {
        return ExtensionLoader.getExtensionLoader(Tolerant.class).getExtension(key);
    }
}
