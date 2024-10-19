package com.sunl888.rocket.common.enums;

import com.sunl888.rocket.common.exception.CompressorException;
import com.sunl888.rocket.common.exception.SerializeException;
import lombok.Getter;

@Getter
public enum CompressorEnum {
    GZIP(1, "gzip");
    private final int code;
    private final String name;

    CompressorEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    static public CompressorEnum getByCode(int code) {
        for (CompressorEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new CompressorException("不支持的压缩算法");
    }

    static public CompressorEnum getByName(String name) {
        for (CompressorEnum value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        throw new SerializeException("不支持的压缩算法");
    }
}
