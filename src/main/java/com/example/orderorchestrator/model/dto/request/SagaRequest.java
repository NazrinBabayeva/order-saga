package com.example.orderorchestrator.model.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SagaRequest {
    private Long orderId;
    private Long productId;
    private Long quantity;
    private Long customerId;
    private String address;
    private BigDecimal price;
    private String email;
}
