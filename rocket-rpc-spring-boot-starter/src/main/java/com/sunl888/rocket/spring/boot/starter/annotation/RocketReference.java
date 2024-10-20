package com.sunl888.rocket.spring.boot.starter.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface RocketReference {
    /**
     * 接口类
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 接口名称
     */
    String interfaceName() default "";

    /**
     * 服务调用超时值
     */
    int timeout() default 3;

    /**
     * 负载均衡策略，合法值包括：random, roundrobin, leastactive
     */
    String loadBalance() default "";

    /**
     * 服务调用重试次数
     */
    int retries() default -1;

    /**
     * 集群策略，合法值包括：failover, failfast, failsafe, failback
     */
    String cluster() default "failfast";

    /**
     * 代理生成方式，合法值包括：jdk, javassist, cglib
     */
    String proxy() default "cglib";

    /**
     * 服务版本
     */
    String version() default "1.0.0";

    /**
     * 服务组
     */
    String group() default "DEFAULT";
}
