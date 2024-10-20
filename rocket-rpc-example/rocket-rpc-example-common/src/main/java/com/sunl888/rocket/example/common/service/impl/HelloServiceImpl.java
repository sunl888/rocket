package com.sunl888.rocket.example.common.service.impl;

import com.sunl888.rocket.example.common.service.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
