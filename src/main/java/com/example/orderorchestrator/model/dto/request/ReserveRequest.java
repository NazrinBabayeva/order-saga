package com.example.orderorchestrator.model.dto.request;

import lombok.Data;

@Data
public class ReserveRequest {
    private Long productId;
    private Long quantity;
}
