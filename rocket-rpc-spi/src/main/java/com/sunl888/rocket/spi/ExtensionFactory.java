package com.sunl888.rocket.spi;

import com.sunl888.rocket.common.annotation.SPI;

@SPI
public interface ExtensionFactory {

    /**
     * Gets Extension.
     *
     * @param <T>   the type parameter
     * @param key   the key
     * @param clazz the clazz
     * @return the extension
     */
    <T> T getExtension(String key, Class<T> clazz);
}