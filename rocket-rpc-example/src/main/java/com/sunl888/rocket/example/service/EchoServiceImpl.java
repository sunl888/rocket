package com.sunl888.rocket.example.service;

public class EchoServiceImpl implements EchoService {
    @Override
    public String echo(String body) {
        return "echo: " + body;
    }
}
