package com.sunl888.rocket.common.enums;

import com.sunl888.rocket.common.exception.RetryException;
import lombok.Getter;

@Getter
public enum RetryEnum {
    NOT_RETRY(0, "不重试"),
    ;
    private final int code;
    private final String name;

    RetryEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    static public RetryEnum getByCode(int code) {
        for (RetryEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new RetryException("不支持的重试策略");
    }

    static public RetryEnum getByName(String name) {
        for (RetryEnum value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        throw new RetryException("不支持的重试策略");
    }
}
