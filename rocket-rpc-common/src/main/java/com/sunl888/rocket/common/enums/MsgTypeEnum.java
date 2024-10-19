package com.sunl888.rocket.common.enums;

import lombok.Getter;

@Getter
public enum MsgTypeEnum {
    /**
     * 普通请求
     */
    REQUEST((byte) 1),

    /**
     * 普通响应
     */
    RESPONSE((byte) 2),

    /**
     * 心跳
     */
    HEARTBEAT((byte) 3),
    ;

    private final byte value;

    MsgTypeEnum(byte value) {
        this.value = value;
    }

}
