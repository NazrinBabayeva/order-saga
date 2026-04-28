package com.example.orderorchestrator.model.dto.response;

import com.example.orderorchestrator.enums.PaymentStatus;

import lombok.Data;

@Data
public class PaymentResponse {
    private Long orderId;
    private PaymentStatus status;
}