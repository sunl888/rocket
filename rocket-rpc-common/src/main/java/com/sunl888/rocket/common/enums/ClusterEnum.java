package com.sunl888.rocket.common.enums;

import com.sunl888.rocket.common.exception.ClusterException;
import lombok.Getter;

@Getter
public enum ClusterEnum {
    FAIL_FAST(1, "failfast"),
    FAIL_OVER(2, "failover"),
    FAIL_BACK(3, "failback"),
    ;
    private final int code;
    private final String name;

    ClusterEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    static public ClusterEnum getByCode(int code) {
        for (ClusterEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new ClusterException("不支持的集群策略");
    }

    static public ClusterEnum getByName(String name) {
        for (ClusterEnum value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        throw new ClusterException("不支持的集群策略");
    }
}
