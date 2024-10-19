package com.sunl888.rocket.compressor.factory;


import com.sunl888.rocket.common.enums.CompressorEnum;
import com.sunl888.rocket.compressor.Compressor;
import com.sunl888.rocket.spi.ExtensionLoader;

public class CompressorFactory {
    public static Compressor getInstance(String key) {
        return ExtensionLoader.getExtensionLoader(Compressor.class).getExtension(key);
    }

    public static Compressor getInstance(int code) {
        return ExtensionLoader.getExtensionLoader(Compressor.class).getExtension(CompressorEnum.getByCode(code).getName());
    }

    public static Compressor getInstance() {
        return ExtensionLoader.getExtensionLoader(Compressor.class).getDefaultExtension();
    }

}
