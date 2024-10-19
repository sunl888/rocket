package com.sunl888.rocket.registry;

import com.sunl888.rocket.common.annotation.Adaptive;
import com.sunl888.rocket.common.config.RegistryConfig;
import com.sunl888.rocket.common.exception.RegistryException;
import com.sunl888.rocket.common.model.ServiceMeta;
import com.sunl888.rocket.common.util.ServiceMetaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Adaptive
public class ZookeeperRegistryService implements RegistryService {
    public static final String NAME = "zookeeper";
    private static final String ZK_ROOT_PATH = "/rocket/register";

    /**
     * 本地注册节点 key 集合 用于维护续期
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 监听的key集合, 用于续期
     */
    private final Set<String> watchingKeySet = new HashSet<>();

    /**
     * zk客户端
     */
    private CuratorFramework client;

    /**
     * 服务发现
     */
    private ServiceDiscovery<ServiceMeta> serviceDiscovery;

    /**
     * 服务列表本地缓存
     */
    private volatile List<ServiceMeta> serviceMetaListCache;

    @Override
    public void init(RegistryConfig registryConfig) {
        // Client 是 Curator 提供的一个类，用于管理与 Zookeeper 的连接，它提供了一些方法用于创建、删除、读取节点等操作。
        client = CuratorFrameworkFactory.builder()
                .connectString(registryConfig.getAddress())
                .retryPolicy(new ExponentialBackoffRetry(Math.toIntExact(registryConfig.getTimeout()),
                        registryConfig.getRetryCount()))
                .build();

        // Discovery 是用于管理服务的注册和发现的组件，它提供了服务注册、服务发现、服务状态监控等功能。
        serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMeta.class)
                .client(client)
                .basePath(ZK_ROOT_PATH)
                .serializer(new JsonInstanceSerializer<>(ServiceMeta.class))
                .build();
        try {
            client.start();
            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RegistryException("注册中心启动失败", e);
        }
    }

    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {
        serviceDiscovery.registerService(buildServiceInstance(serviceMeta));
        localRegisterNodeKeySet.add(getZkRegisterKey(serviceMeta));
        log.info("服务注册成功: {}", serviceMeta);
    }

    @Override
    public void unregister(ServiceMeta serviceMeta) {
        try {
            serviceDiscovery.unregisterService(buildServiceInstance(serviceMeta));
            localRegisterNodeKeySet.remove(getZkRegisterKey(serviceMeta));
            log.info("服务注销成功: {}", serviceMeta);
        } catch (Exception e) {
            throw new RegistryException(e);
        }
    }


    @Override
    public List<ServiceMeta> serviceDiscovery(String serviceName) {
        if (serviceMetaListCache != null && !serviceMetaListCache.isEmpty()) {
            return serviceMetaListCache;
        }
        try {
            serviceMetaListCache = serviceDiscovery.queryForInstances(serviceName)
                    .stream()
                    .map(ServiceInstance::getPayload)
                    .toList();

            return serviceMetaListCache;
        } catch (Exception e) {
            throw new RegistryException(e);
        }
    }

    @Override
    public void destroy() {
        log.info("服务开始下线");
        try (CuratorFramework c = this.client) {
            for (String node : localRegisterNodeKeySet) {
                try {
                    c.delete().forPath(node);
                } catch (Exception e) {
                    throw new RegistryException("服务下线失败", e);
                }
            }
        }
    }

    @Override
    public void watch(ServiceMeta serviceMeta) {
        String zkRegisterKey = getZkRegisterKey(serviceMeta);
        boolean exists = watchingKeySet.add(zkRegisterKey);
        if (!exists) {
            // forDeletes()方法用于监听节点的删除事件，当节点被删除时，会触发监听器的回调方法。
            // forChanges()方法用于监听节点的变化事件，当节点的数据发生变化时，会触发监听器的回调方法。
            // curatorCache是Curator提供的一个类，用于监听节点的变化，包括节点的增加、删除、数据的变化等。
            CuratorCache curatorCache = CuratorCache.build(client, zkRegisterKey);
            curatorCache.start();

            curatorCache.listenable()
                    .addListener(
                            CuratorCacheListener.builder()
                                    .forDeletes(_ -> serviceMetaListCache.clear())
                                    .forChanges((_, _) -> serviceMetaListCache.clear())
                                    .build()
                    );
        }
    }


    /**
     * ServiceInstance 是 Curator 提供的一个类，用于描述一个服务实例的信息，包括服务名称、服务地址、服务端口等。
     *
     * @param serviceMeta 服务元数据
     * @return ServiceInstance
     * @throws Exception 异常
     */
    private ServiceInstance<ServiceMeta> buildServiceInstance(ServiceMeta serviceMeta) throws Exception {
        String serviceKey = ServiceMetaUtil.buildServiceKey(serviceMeta);
        String simpleServiceAddress = ServiceMetaUtil.buildSimpleServiceAddress(serviceMeta);
        return ServiceInstance
                .<ServiceMeta>builder()
                .id(simpleServiceAddress)
                .name(serviceKey)
                .address(simpleServiceAddress)
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();

    }

    private String getZkRegisterKey(ServiceMeta serviceMeta) {
        return ZK_ROOT_PATH + "/" + ServiceMetaUtil.buildServiceNodeKey(serviceMeta);
    }
}
