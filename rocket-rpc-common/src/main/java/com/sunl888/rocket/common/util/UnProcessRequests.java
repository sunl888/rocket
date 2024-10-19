package com.sunl888.rocket.common.util;

import com.sunl888.rocket.common.model.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class UnProcessRequests {
    private static final Map<Long, CompletableFuture<RpcResponse>> FUTURE_MAP = new ConcurrentHashMap<>();

    public static void put(long requestId, CompletableFuture<RpcResponse> future) {
        FUTURE_MAP.put(requestId, future);
    }

    public static void complete(RpcResponse rpcResponse) {
        CompletableFuture<RpcResponse> future = FUTURE_MAP.remove(rpcResponse.getRequestId());
        if (future == null) {
            throw new IllegalStateException("异步请求上下文不存在：" + rpcResponse);
        }
        future.complete(rpcResponse);
    }
}
