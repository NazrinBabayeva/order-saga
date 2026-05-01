package com.example.orderorchestrator.client;

import com.example.orderorchestrator.model.dto.request.PaymentRequest;
import com.example.orderorchestrator.model.dto.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-ms", url = "http://localhost:8088/payment-ms/api")
public interface PaymentClient {

    @PostMapping("/v1/payments")
    PaymentResponse pay(@RequestBody PaymentRequest request);
}
