package com.sunl888.rocket.core.remoting;


import com.sunl888.rocket.common.exception.ConsumerException;
import com.sunl888.rocket.core.remoting.handler.NettyClientHandler;
import com.sunl888.rocket.core.remoting.protocol.ProtocolDecoder;
import com.sunl888.rocket.core.remoting.protocol.ProtocolEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyClient {
    private final Bootstrap bootstrap;
    private final ConcurrentHashMap<SocketAddress, Channel> channelCache = new ConcurrentHashMap<>(8);

    private NettyClient() {
        bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                // 10秒未触发channelRead就会触发 IdleStateEvent.READER_IDLE 事件。
                                // 10秒未触发writeAndFlush / write就会触发 IdleStateEvent.WRITER_IDLE 事件。
                                // 30秒未触发以上所有操作就会触发 IdleStateEvent.ALL_IDLE 事件。
                                .addLast(new IdleStateHandler(10, 10, 30, TimeUnit.SECONDS))
                                .addLast(new ProtocolEncoder())
                                .addLast(new ProtocolDecoder())
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    public static NettyClient getInstance() {
        return InstanceHolder.instance;
    }

    public Channel getChannel(SocketAddress address) {
        Channel channel = channelCache.get(address);
        if (channel == null || !channel.isActive()) {
            channel = connect(address);
            channelCache.put(address, channel);
        } else {
            // 确保 Channel 是活跃的
            if (!channel.isActive()) {
                channelCache.remove(address);
                channel = connect(address);
                channelCache.put(address, channel);
            }
        }
        return channel;
    }

    public void removeChannel(SocketAddress address) {
        channelCache.remove(address);
    }

    private Channel connect(SocketAddress address) {
        try {
            log.info("尝试连接服务端 [{}]", address);
            CompletableFuture<Channel> completableFuture = new CompletableFuture<>();

            ChannelFuture connect = bootstrap.connect(address);
            connect.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("连接成功 [{}]", address);
                    completableFuture.complete(future.channel());
                } else {
                    log.warn("连接失败 [{}]: {}", address, future.cause().getMessage());
                    completableFuture.completeExceptionally(new IllegalStateException("连接失败，地址:" + address));
                }
            });

            return completableFuture.get(10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            throw new ConsumerException(address + " 连接失败.", ex);
        }
    }

    private static final class InstanceHolder {
        private static final NettyClient instance = new NettyClient();
    }
}
