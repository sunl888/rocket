package com.sunl888.rocket.serialization.factory;


import com.sunl888.rocket.common.enums.SerializerEnum;
import com.sunl888.rocket.serialization.Serializer;
import com.sunl888.rocket.spi.ExtensionLoader;

public class SerializerFactory {
    public static Serializer getInstance(String key) {
        return ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(key);
    }

    public static Serializer getInstance(SerializerEnum serializerEnum) {
        return ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(serializerEnum.getName());
    }

    public static Serializer getInstance(byte code) {
        return getInstance(SerializerEnum.getByCode(code));
    }
}
