package com.sunl888.rocket.serialization;


import com.sunl888.rocket.common.annotation.SPI;

import java.io.IOException;

@SPI(JdkSerializer.NAME)
public interface Serializer {

    <T> byte[] serialize(T obj) throws IOException;

    <T> T deserialize(byte[] data, Class<T> clazz) throws IOException;
}
