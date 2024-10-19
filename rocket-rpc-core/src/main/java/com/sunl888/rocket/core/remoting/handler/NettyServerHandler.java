package com.sunl888.rocket.core.remoting.handler;


import com.sunl888.rocket.common.constants.ProtocolConstant;
import com.sunl888.rocket.common.enums.MsgTypeEnum;
import com.sunl888.rocket.common.model.RpcMessage;
import com.sunl888.rocket.common.model.RpcRequest;
import com.sunl888.rocket.common.model.RpcResponse;
import com.sunl888.rocket.registry.LocalRegistryCache;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcMessage<?>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage<?> rpcMessage) throws Exception {
        log.debug("【服务端】接收到反序列化后的消息: {}", rpcMessage);

        // 只处理请求类型的消息，非请求类型的消息直接丢弃
        if (rpcMessage.getHeader().getType() != MsgTypeEnum.REQUEST.getValue()) {
            log.warn("非请求类型的消息被丢弃");
            return;
        }

        if (!(rpcMessage.getBody() instanceof RpcRequest rpcRequest)) {
            log.warn("消息体不是 Request 类型");
            return;
        }

        try {
            // 获取实现类并通过反射调用方法
            Class<?> implClass = LocalRegistryCache.get(rpcRequest.getServiceName());
            Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            Object result = method.invoke(implClass.getDeclaredConstructor().newInstance(), rpcRequest.getArgs());

            // 构建响应
            RpcResponse rpcResponse = RpcResponse.builder()
                    .requestId(rpcMessage.getHeader().getRequestId())
                    .data(result)
                    .dataType(method.getReturnType())
                    .message("success")
                    .build();
            // 构建响应头
            RpcMessage.Header header = RpcMessage.Header.builder()
                    .requestId(rpcMessage.getHeader().getRequestId())
                    .magic(ProtocolConstant.MAGIC)
                    .type(MsgTypeEnum.RESPONSE.getValue())
                    .serialize(rpcMessage.getHeader().getSerialize())
                    .compress(rpcMessage.getHeader().getCompress())
                    .build();

            // 构建完整的响应
            RpcMessage<?> response = RpcMessage.builder()
                    .body(rpcResponse)
                    .header(header)
                    .build();
            // 发送响应
            channelHandlerContext.writeAndFlush(response)
                    .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            log.info("服务端向调用方回写响应结果: {}", response);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.error("处理请求时发生异常: {}", e.getMessage());
            // TODO 发送异常响应到客户端
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.warn("长时间未收到心跳包，关闭不活跃的连接");
                ctx.close();
            }
            return;
        }
        // 其他事件
        super.userEventTriggered(ctx, evt);
    }
}
