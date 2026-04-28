package com.example.orderorchestrator.service.client;

import com.example.orderorchestrator.model.dto.request.PaymentRequest;
import com.example.orderorchestrator.model.dto.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-ms", url = "http://localhost:8088")
public interface PaymentClient {

    @PostMapping("/v1/payments")
    PaymentResponse pay(@RequestBody PaymentRequest request);
}
