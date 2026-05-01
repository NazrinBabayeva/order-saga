package com.example.orderorchestrator.client;

import com.example.orderorchestrator.enums.OrderStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-ms", url = "http://localhost:8081/order-ms/api")
public interface OrderClient {

    @PutMapping("/v1/orders/{orderId}/status")
    void updateStatus(
            @PathVariable("orderId") Long orderId,
            @RequestParam("status") OrderStatus status
    );
}
