package com.sunl888.rocket.core.retry;

import com.sunl888.rocket.common.annotation.Adaptive;
import com.sunl888.rocket.common.model.RpcResponse;

import java.util.concurrent.Callable;

@Adaptive
public class NotRetryer implements Retryable {
    public static final String NAME = "not";

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
