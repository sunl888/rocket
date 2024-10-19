package com.sunl888.rocket.core.invoker;


import com.sunl888.rocket.common.annotation.Adaptive;
import com.sunl888.rocket.common.config.ConfigManager;
import com.sunl888.rocket.common.config.ProviderConfig;
import com.sunl888.rocket.common.config.RocketConfig;
import com.sunl888.rocket.common.constants.ProtocolConstant;
import com.sunl888.rocket.common.constants.TolerantConstant;
import com.sunl888.rocket.common.enums.CompressorEnum;
import com.sunl888.rocket.common.enums.MsgTypeEnum;
import com.sunl888.rocket.common.enums.SerializerEnum;
import com.sunl888.rocket.common.exception.InvokerException;
import com.sunl888.rocket.common.model.*;
import com.sunl888.rocket.common.util.ServiceMetaUtil;
import com.sunl888.rocket.common.util.UnProcessRequests;
import com.sunl888.rocket.core.factory.RetryableFactory;
import com.sunl888.rocket.core.factory.TolerantFactory;
import com.sunl888.rocket.core.remoting.NettyClient;
import com.sunl888.rocket.core.retry.Retryable;
import com.sunl888.rocket.core.tolerant.Tolerant;
import com.sunl888.rocket.loadbalancer.LoadBalancer;
import com.sunl888.rocket.loadbalancer.factory.LoadBalancerFactory;
import com.sunl888.rocket.registry.RegistryService;
import com.sunl888.rocket.registry.factory.RegistryServiceFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.data.id.IdUtil;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Adaptive
public class Netty4Invoker implements Invoker {
    public static final String NAME = "netty4";

    private final NettyClient nettyClient = NettyClient.getInstance();

    @Override
    public RpcResponse invoke(RpcRequest request) throws Exception {
        return (RpcResponse) invokeAsync(request).getData();
    }

    @Override
    public RpcResponseFuture invokeAsync(RpcRequest request) throws Exception {
        return doInvoke(request);
    }

    RpcResponseFuture doInvoke(RpcRequest request) throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();
        // 从注册中心获取服务提供者请求地址
        RocketConfig rocketConfig = configManager.getRocketConfig();
        ProviderConfig providerConfig = configManager.getProviderConfig();

        RegistryService registryService = RegistryServiceFactory.getInstance(rocketConfig.getRegistry());

        String serviceKey = ServiceMetaUtil.buildServiceKey(request.getServiceName(), providerConfig.getVersion());
        List<ServiceMeta> serviceMetaList = registryService.serviceDiscovery(serviceKey);
        if (CollUtil.isEmpty(serviceMetaList)) {
            throw new RuntimeException("暂无可用服务提供者");
        }

        // 负载均衡
        HashMap<String, Object> requestParams = new HashMap<>();
        requestParams.put("serviceName", request.getServiceName());
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rocketConfig.getLoadBalancer());
        ServiceMeta selectedServiceMeta = loadBalancer.select(requestParams, serviceMetaList);
        InetSocketAddress socketAddress = new InetSocketAddress(selectedServiceMeta.getServiceHost(), selectedServiceMeta.getServicePort());

        Retryable retryer = RetryableFactory.getInstance(rocketConfig.getRetryer());
        RpcResponse response;
        try {
            // 重试
            response = retryer.doRetry(() -> {
                Channel channel = nettyClient.getChannel(socketAddress);
                if (!channel.isActive()) {
                    throw new InvokerException("通道非激活状态: " + socketAddress);
                }
                CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
                int serializer = SerializerEnum.getByName(rocketConfig.getSerializer()).getCode();
                int compressor = CompressorEnum.getByName(rocketConfig.getCompressor()).getCode();

                RpcMessage<?> rpcMessage = buildMessage(request, serializer, compressor);
                UnProcessRequests.put(rpcMessage.getHeader().getRequestId(), resultFuture);
                channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        log.info("客户端发送消息成功: {}", rpcMessage);
                        return;
                    }
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.info("客户端发送失败: ", future.cause());
                });
                return resultFuture.get();
            });
        } catch (Exception e) {
            // 容错
            Tolerant tolerant = TolerantFactory.getInstance(rocketConfig.getCluster());
            Map<String, Object> context = new HashMap<>();
            context.put(TolerantConstant.SERVICE_LIST, serviceMetaList);
            context.put(TolerantConstant.CURRENT_SERVICE, selectedServiceMeta);
            context.put(TolerantConstant.RPC_REQUEST, request);
            response = tolerant.doTolerant(context, e);
        }
        return new RpcResponseFuture(CompletableFuture.completedFuture(response));
    }

    RpcMessage<?> buildMessage(RpcRequest request, int serializer, int compress) {
        RpcMessage.Header header = RpcMessage.Header.builder()
                .status((byte) 0)
                .compress((byte) compress)
                .serialize((byte) serializer)
                .magic(ProtocolConstant.MAGIC)
                .version(ProtocolConstant.VERSION)
                .type(MsgTypeEnum.REQUEST.getValue())
                .requestId(IdUtil.getSnowflakeNextId())
                .length(request.toString().getBytes().length)
                .build();
        return RpcMessage.builder().header(header).body(request).build();
    }

}
