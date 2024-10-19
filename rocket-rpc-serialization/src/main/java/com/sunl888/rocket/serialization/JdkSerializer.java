package com.sunl888.rocket.serialization;

import com.sunl888.rocket.common.annotation.Adaptive;
import com.sunl888.rocket.common.exception.SerializeException;

import java.io.*;

@Adaptive
public class JdkSerializer implements Serializer {
    public static final String NAME = "jdk";

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        // 这是一个字节数组输出流，数据会被写到一个字节数组中
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 将对象写入到字节数组输出流中
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new SerializeException("反序列化失败", e);
        }
    }
}
