package com.example.orderorchestrator.config;

import feign.Request;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {

    /**
     * ⏱ Feign timeout configuration
     * connectTimeout → service-ə qoşulma vaxtı
     * readTimeout → response gözləmə vaxtı
     */
    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(
                3000, TimeUnit.MILLISECONDS,
                5000, TimeUnit.MILLISECONDS,
                true
        );
    }
}