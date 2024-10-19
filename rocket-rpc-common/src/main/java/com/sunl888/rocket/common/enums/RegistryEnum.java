package com.sunl888.rocket.common.enums;

import com.sunl888.rocket.common.exception.ClusterException;
import com.sunl888.rocket.common.exception.RegistryException;
import lombok.Getter;

@Getter
public enum RegistryEnum {
    ZOOKEEPER(1, "zookeeper");
    private final int code;
    private final String name;

    RegistryEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    static public RegistryEnum getByCode(int code) {
        for (RegistryEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new RegistryException("不支持的注册中心");
    }

    static public RegistryEnum getByName(String name) {
        for (RegistryEnum value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        throw new ClusterException("不支持的注册中心");
    }
}
