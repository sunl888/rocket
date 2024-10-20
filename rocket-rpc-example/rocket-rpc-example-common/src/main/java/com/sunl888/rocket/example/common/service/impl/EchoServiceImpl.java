package com.sunl888.rocket.example.common.service.impl;

import com.sunl888.rocket.example.common.service.EchoService;

public class EchoServiceImpl implements EchoService {
    @Override
    public String echo(String body) {
        return "echo: " + body;
    }
}
