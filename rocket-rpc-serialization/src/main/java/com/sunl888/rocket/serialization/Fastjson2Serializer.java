package com.sunl888.rocket.serialization;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.sunl888.rocket.common.annotation.Adaptive;

import java.io.IOException;

@Adaptive
public class Fastjson2Serializer implements Serializer {
    public static final String NAME = "fastjson2";

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        return JSON.parseObject(data, clazz, JSONReader.Feature.SupportClassForName);
    }
}
