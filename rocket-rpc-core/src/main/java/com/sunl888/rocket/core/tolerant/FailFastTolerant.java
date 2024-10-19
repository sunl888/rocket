package com.sunl888.rocket.core.tolerant;


import com.sunl888.rocket.common.annotation.Adaptive;
import com.sunl888.rocket.common.exception.ConsumerException;
import com.sunl888.rocket.common.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Adaptive
public class FailFastTolerant implements Tolerant {
    public static final String NAME = "not";

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new ConsumerException("请求失败", e);
    }
}