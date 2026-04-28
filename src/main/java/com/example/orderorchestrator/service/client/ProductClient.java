package com.example.orderorchestrator.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-ms", url = "http://localhost:8082")
public interface ProductClient {

    @PostMapping("/v1/products/reserve")
    void reserve(@RequestParam Long productId,
                 @RequestParam Long quantity);

    @PostMapping("/v1/products/release")
    void release(@RequestParam Long productId,
                 @RequestParam Long quantity);
}
