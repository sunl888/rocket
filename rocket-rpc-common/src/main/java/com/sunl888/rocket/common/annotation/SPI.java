package com.sunl888.rocket.common.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {

    /**
     * 指定一个默认的实现
     *
     * @return the string
     */
    String value() default "";
}