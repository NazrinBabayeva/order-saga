package com.example.orderorchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrderOrchestrator {

    public static void main(String[] args) {
        SpringApplication.run(OrderOrchestrator.class, args);
    }

}
