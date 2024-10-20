package com.sunl888.rocket.spring.boot.starter.annotation;

import com.sunl888.rocket.spring.boot.starter.RocketRpcAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(RocketRpcAutoConfiguration.class)
public @interface EnableRocket {

    String[] scanPackages() default {};
}
