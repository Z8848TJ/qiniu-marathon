package com.paper.sword;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class GateApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateApplication.class, args);
    }
}