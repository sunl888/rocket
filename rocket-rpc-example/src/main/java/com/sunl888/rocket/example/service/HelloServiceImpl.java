package com.sunl888.rocket.example.service;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
