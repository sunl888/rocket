package com.sunl888.rocket.core.remoting;


import com.sunl888.rocket.common.config.ConfigManager;
import com.sunl888.rocket.common.config.ProviderConfig;
import com.sunl888.rocket.common.exception.ProviderException;
import com.sunl888.rocket.core.remoting.handler.NettyServerHandler;
import com.sunl888.rocket.core.remoting.protocol.ProtocolDecoder;
import com.sunl888.rocket.core.remoting.protocol.ProtocolEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.thread.ThreadUtil;

import java.util.concurrent.ThreadFactory;

@Slf4j
public class NettyServer implements Server {
    private final ThreadFactory threadFactory = ThreadUtil.newNamedThreadFactory("serverHandlerGroup", false);

    @Override
    public void start(int port) {
        ProviderConfig providerConfig = ConfigManager.getInstance().getProviderConfig();

        try (EventLoopGroup bossGroup = new NioEventLoopGroup(providerConfig.getNThreads());
             EventLoopGroup workerGroup = new NioEventLoopGroup();
             DefaultEventExecutorGroup serverHandlerGroup = new DefaultEventExecutorGroup(providerConfig.getThreads(), threadFactory)) {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.TRACE))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
//                                    .addLast(new IdleStateHandler(10, 20, 60, TimeUnit.SECONDS))
                                    .addLast(new ProtocolEncoder())
                                    .addLast(new ProtocolDecoder())
                                    .addLast(serverHandlerGroup, new NettyServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new ProviderException("RocketRPC 服务启动失败", e);
        }
    }

    @Override
    public void stop() {

    }


}
