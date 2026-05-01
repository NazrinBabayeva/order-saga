package com.example.orderorchestrator.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SagaRequest {
    private Long orderId;
    private Long productId;
    private Long quantity;
    private Long customerId;
    private String address;
    private BigDecimal price;
    private String email;
}
