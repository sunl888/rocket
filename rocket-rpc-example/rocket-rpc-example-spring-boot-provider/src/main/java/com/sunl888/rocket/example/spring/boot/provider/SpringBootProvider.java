package com.sunl888.rocket.example.spring.boot.provider;

import com.sunl888.rocket.spring.boot.starter.annotation.EnableRocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRocket
public class SpringBootProvider {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootProvider.class, args);
    }
}
