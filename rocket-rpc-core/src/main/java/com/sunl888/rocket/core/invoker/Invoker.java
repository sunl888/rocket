package com.sunl888.rocket.core.invoker;

import com.sunl888.rocket.common.annotation.SPI;
import com.sunl888.rocket.common.model.RpcRequest;
import com.sunl888.rocket.common.model.RpcResponse;
import com.sunl888.rocket.common.model.RpcResponseFuture;

@SPI(Netty4Invoker.NAME)
public interface Invoker {

    RpcResponse invoke(RpcRequest request) throws Exception;

    RpcResponseFuture invokeAsync(RpcRequest request) throws Exception;

}
