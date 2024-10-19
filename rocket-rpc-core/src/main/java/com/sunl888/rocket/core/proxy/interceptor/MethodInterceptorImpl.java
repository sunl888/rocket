package com.sunl888.rocket.core.proxy.interceptor;

import com.sunl888.rocket.common.config.ConfigManager;
import com.sunl888.rocket.common.config.RocketConfig;
import com.sunl888.rocket.common.model.RpcRequest;
import com.sunl888.rocket.common.model.RpcResponse;
import com.sunl888.rocket.common.model.RpcResponseFuture;
import com.sunl888.rocket.core.factory.InvokerFactory;
import com.sunl888.rocket.core.invoker.Invoker;

import java.lang.reflect.Method;

public class MethodInterceptorImpl implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args) throws Throwable {
        RocketConfig rocketConfig = ConfigManager.getInstance().getRocketConfig();
        Invoker invoker = InvokerFactory.getInvoker(rocketConfig.getInvoker());

        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        RpcResponseFuture invoke = invoker.invokeAsync(rpcRequest);

        return ((RpcResponse) invoke.getData()).getData();
    }
}
