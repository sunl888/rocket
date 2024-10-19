package com.sunl888.rocket.common.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Data
@Slf4j
public class RpcResponseFuture implements Serializable {
    private final CompletableFuture<?> future;

    public RpcResponseFuture(CompletableFuture<?> future) {
        this.future = future;
    }

    public Object getData() {
        try {
            return future.get();
        } catch (Exception e) {
            log.info("获取异步响应失败：", e);
        }
        return null;
    }
}
