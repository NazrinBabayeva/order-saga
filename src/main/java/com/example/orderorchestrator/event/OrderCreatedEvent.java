package com.example.orderorchestrator.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderCreatedEvent {
    private Long orderId;
    private Long productId;
    private int quantity;
    private BigDecimal price;
    private String email;
    private String status;


}

