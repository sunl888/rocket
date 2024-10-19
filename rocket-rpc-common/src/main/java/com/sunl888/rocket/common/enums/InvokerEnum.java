package com.sunl888.rocket.common.enums;

import com.sunl888.rocket.common.exception.InvokerException;
import lombok.Getter;

@Getter
public enum InvokerEnum {
    Netty4(1, "netty4"),

    ;
    private final int code;
    private final String name;

    InvokerEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    static public InvokerEnum getByCode(int code) {
        for (InvokerEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new InvokerException("不支持的执行器");
    }

    static public InvokerEnum getByName(String name) {
        for (InvokerEnum value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        throw new InvokerException("不支持的执行器");
    }
}
