package com.example.orderorchestrator.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-ms", url = "http://localhost:8081")
public interface OrderClient {

    @PutMapping("/v1/orders/{id}/status")
    void updateStatus(@PathVariable Long id,
                      @RequestParam String status);
}
