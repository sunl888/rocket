package com.sunl888.rocket.common.enums;

import com.sunl888.rocket.common.exception.SerializeException;
import lombok.Getter;

@Getter
public enum SerializerEnum {
    JDK(1, "jdk"),
    FASTJSON2(2, "fastjson2"),
    ;

    private final int code;
    private final String name;

    SerializerEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    static public SerializerEnum getByCode(int code) {
        for (SerializerEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new SerializeException("不支持的序列化器");
    }

    static public SerializerEnum getByName(String name) {
        for (SerializerEnum value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        throw new SerializeException("不支持的序列化器");
    }

}
