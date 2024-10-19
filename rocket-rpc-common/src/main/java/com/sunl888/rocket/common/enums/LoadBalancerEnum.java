package com.sunl888.rocket.common.enums;

import com.sunl888.rocket.common.exception.LoadBalancerException;
import lombok.Getter;

@Getter
public enum LoadBalancerEnum {
    ROUND_ROBIN(1, "roundRobin"),
    LEAST_ACTIVE(2, "leastActive"),
    RANDOM(3, "random"),
    ;
    private final int code;
    private final String name;

    LoadBalancerEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    static public LoadBalancerEnum getByCode(int code) {
        for (LoadBalancerEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new LoadBalancerException("不支持的负载均衡算法");
    }

    static public LoadBalancerEnum getByName(String name) {
        for (LoadBalancerEnum value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        throw new LoadBalancerException("不支持的负载均衡算法");
    }
}
