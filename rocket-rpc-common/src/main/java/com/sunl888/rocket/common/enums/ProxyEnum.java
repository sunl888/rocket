package com.sunl888.rocket.common.enums;

import com.sunl888.rocket.common.exception.ProxyException;
import lombok.Getter;

@Getter
public enum ProxyEnum {
    JDK(1, "jdk"),
    CGLIB(2, "cglib");
    private final int code;
    private final String name;

    ProxyEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    static public ProxyEnum getByCode(int code) {
        for (ProxyEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new ProxyException("不支持的代理方式");
    }

    static public ProxyEnum getByName(String name) {
        for (ProxyEnum value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        throw new ProxyException("不支持的代理方式");
    }
}
