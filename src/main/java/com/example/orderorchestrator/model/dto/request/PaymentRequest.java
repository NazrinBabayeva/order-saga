package com.example.orderorchestrator.model.dto.request;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long orderId;
    private Long customerId;
    private BigDecimal amount;
}
