package com.sunl888.rocket.core.factory;

import com.sunl888.rocket.core.retry.Retryable;
import com.sunl888.rocket.spi.ExtensionLoader;

public class RetryableFactory {
    public static Retryable getInstance(String key) {
        return ExtensionLoader.getExtensionLoader(Retryable.class).getExtension(key);
    }
}
