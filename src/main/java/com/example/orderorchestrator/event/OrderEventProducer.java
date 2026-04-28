package com.example.orderorchestrator.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "order-events";

    public void sendOrderCompletedEvent(Long orderId,
                                        Long productId,
                                        int quantity,
                                        BigDecimal price,
                                        String email) {

        OrderCreatedEvent event = new OrderCreatedEvent(
                orderId,
                productId,
                quantity,
                price,
                email,
                "COMPLETED"
        );

        kafkaTemplate.send(TOPIC, event);
    }

    public void sendOrderFailedEvent(Long orderId,
                                     Long productId,
                                     int quantity,
                                     BigDecimal price,
                                     String email) {

        OrderCreatedEvent event = new OrderCreatedEvent(
                orderId,
                productId,
                quantity,
                price,
                email,
                "FAILED"
        );
    }
}