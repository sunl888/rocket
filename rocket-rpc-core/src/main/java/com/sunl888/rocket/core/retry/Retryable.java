package com.sunl888.rocket.core.retry;

import com.sunl888.rocket.common.annotation.SPI;
import com.sunl888.rocket.common.model.RpcResponse;

import java.util.concurrent.Callable;

@SPI(NotRetryer.NAME)
public interface Retryable {

    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;

}
