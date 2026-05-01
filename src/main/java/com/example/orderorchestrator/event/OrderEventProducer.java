package com.example.orderorchestrator.event;

import com.example.orderorchestrator.model.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "succeded-order-events";

    public void sendOrderCompletedEvent(Long orderId,
                                        Long productId,
                                        int quantity,
                                        BigDecimal price,
                                        String email,
                                        Long userId) {

        OrderCreatedEvent event = new OrderCreatedEvent(
                orderId,
                productId,
                quantity,
                price,
                email,
                "COMPLETED",
                userId

        );

        kafkaTemplate.send(TOPIC, event);
    }

    public void sendOrderFailedEvent(Long orderId,
                                     Long productId,
                                     int quantity,
                                     BigDecimal price,
                                     String email,
                                     Long userId) {

        OrderCreatedEvent event = new OrderCreatedEvent(
                orderId,
                productId,
                quantity,
                price,
                email,
                "FAILED",
                userId
        );
    }
}