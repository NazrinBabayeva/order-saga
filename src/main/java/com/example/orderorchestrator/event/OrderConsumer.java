package com.example.orderorchestrator.event;

import com.example.orderorchestrator.model.dto.OrderCreatedEvent;
import com.example.orderorchestrator.model.dto.request.SagaRequest;
import com.example.orderorchestrator.service.SagaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final SagaService sagaService;

    @KafkaListener(
            topics = "${spring.kafka.template.order-create-event-topic}",
            groupId = "${spring.kafka.template.order-create-event-group-id}"
    )
    public void consume(OrderCreatedEvent event) {

        log.info("Event is consumed with body={}", event);
        SagaRequest request = new SagaRequest();
        request.setOrderId(event.getOrderId());
        request.setProductId(event.getProductId());
        request.setQuantity((long) event.getQuantity());
        request.setPrice(event.getPrice());
        request.setEmail(event.getEmail());
        request.setCustomerId(event.getUserId());

        sagaService.startSaga(request);
    }
}