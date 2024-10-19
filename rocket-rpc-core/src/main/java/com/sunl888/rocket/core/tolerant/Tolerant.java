package com.sunl888.rocket.core.tolerant;


import com.sunl888.rocket.common.annotation.SPI;
import com.sunl888.rocket.common.model.RpcResponse;

import java.util.Map;

@SPI(FailFastTolerant.NAME)
public interface Tolerant {
    RpcResponse doTolerant(Map<String, Object> context, Exception e);
}
