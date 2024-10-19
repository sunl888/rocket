package com.sunl888.rocket.common.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface RocketService {
    /**
     * 接口实现类
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 接口名称
     */
    String interfaceName() default "";

    /**
     * 服务版本
     */
    String version() default "";

    /**
     * 服务组
     */
    String group() default "";

    /**
     * 服务注册延迟时间，单位毫秒，默认值是-1，表示不延迟
     */
    int delay() default -1;

    /**
     * 集群策略，合法值包括：failover, failfast, failsafe, failback
     */
    String cluster() default "";

    /**
     * 代理生成方式，合法值包括：jdk, javassist, cglib
     */
    String proxy() default "";

    /**
     * 服务端执行重试次数，默认值是-1，表示不重试
     */
    int retries() default -1;

    /**
     * 服务端超时时间，单位毫秒，默认值是-1，表示不超时
     */
    int timeout() default -1;

    /**
     * 序列化类型，合法值包括：fastjson, hessian, kryo, protobuf, jdk
     */
    String serialization() default "fastjson";
}
