package com.sunl888.rocket.core.remoting.handler;

import com.sunl888.rocket.common.config.ConfigManager;
import com.sunl888.rocket.common.config.RocketConfig;
import com.sunl888.rocket.common.constants.ProtocolConstant;
import com.sunl888.rocket.common.enums.CompressorEnum;
import com.sunl888.rocket.common.enums.MsgTypeEnum;
import com.sunl888.rocket.common.enums.SerializerEnum;
import com.sunl888.rocket.common.model.RpcMessage;
import com.sunl888.rocket.common.model.RpcResponse;
import com.sunl888.rocket.common.util.UnProcessRequests;
import com.sunl888.rocket.core.remoting.NettyClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcMessage<?>> {
    private volatile boolean connected = false;

    @Override
    protected void channelRead0(ChannelHandlerContext context, RpcMessage rpcMessage) {
        log.debug("客户端接收到消息: [{}]", rpcMessage);

        // 只处理响应类型的消息，非响应类型的消息直接丢弃
        if (rpcMessage.getHeader().getType() != MsgTypeEnum.RESPONSE.getValue()) {
            log.warn("当前消息将被丢弃");
            return;
        }

        RpcResponse response = (RpcResponse) rpcMessage.getBody();
        UnProcessRequests.complete(response);
        log.info("客户端处理响应结果完成");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("连接丢失，尝试重新连接到 [{}]", ctx.channel().remoteAddress());
        connected = false;  // 重置状态

        // 移除失效的 Channel
        NettyClient.getInstance().removeChannel(ctx.channel().remoteAddress());

        // 尝试重连
        attemptReconnect(ctx.channel().remoteAddress());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                // 检查连接状态，确保不会重复发送
                if (!ctx.channel().isActive()) {
                    log.warn("Channel 不活跃，停止发送心跳");
                }
                log.info("准备发送心跳 [{}]", ctx.channel().remoteAddress());
                RocketConfig rocketConfig = ConfigManager.getInstance().getRocketConfig();
                RpcMessage.Header header = RpcMessage.Header
                        .builder()
                        .magic(ProtocolConstant.MAGIC)
                        .serialize((byte) SerializerEnum.getByName(rocketConfig.getSerializer()).getCode())
                        .compress((byte) CompressorEnum.getByName(rocketConfig.getCompressor()).getCode())
                        .type(MsgTypeEnum.HEARTBEAT.getValue())
                        .build();

                ctx.channel().writeAndFlush(new RpcMessage<>(header, null))
                        .addListener(future -> {
                            if (future.isSuccess()) {
                                log.info("心跳发送成功 [{}]", ctx.channel().remoteAddress());
                            } else {
                                log.warn("心跳发送失败: {}", future.cause().getMessage());
                            }
                        });
            }
            return;
        }
        // 其他事件
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("发生异常: ", cause);
        // 关闭通道
        ctx.close();
    }

    private void attemptReconnect(SocketAddress address) {
        if (!connected) {
            // 防止重复连接
            connected = true;
            try (ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor()) {
                executorService.schedule(() -> {
                    try {
                        Channel channel = NettyClient.getInstance().getChannel(address);
                        if (channel.isActive()) {
                            log.info("成功重连到 [{}]", address);
                        } else {
                            log.warn("重连失败，Channel 仍然不活跃 [{}]", address);
                            // 重置状态以允许后续重连
                            connected = false;
                        }
                    } catch (Exception e) {
                        log.error("重连过程中发生异常", e);
                        // 重置状态
                        connected = false;
                    }
                    // 等待5秒后再重试
                }, 5, TimeUnit.SECONDS);
            }
        }
    }

}
