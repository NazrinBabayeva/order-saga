package com.example.orderorchestrator.service.impl;

import com.example.orderorchestrator.enums.OrderStatus;
import com.example.orderorchestrator.enums.PaymentStatus;
import com.example.orderorchestrator.event.OrderEventProducer;
import com.example.orderorchestrator.model.dto.request.PaymentRequest;
import com.example.orderorchestrator.model.dto.request.SagaRequest;
import com.example.orderorchestrator.service.SagaService;
import com.example.orderorchestrator.service.client.OrderClient;
import com.example.orderorchestrator.service.client.PaymentClient;
import com.example.orderorchestrator.service.client.ProductClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class SagaServiceImpl implements SagaService {

    private final ProductClient productClient;
    private final PaymentClient paymentClient;
    private final OrderClient orderClient;
    private final OrderEventProducer orderEventProducer;

    @Override
    public void startSaga(SagaRequest request) {

        boolean productReserved = false;

        try {
            orderClient.updateStatus(
                    request.getOrderId(),
                    OrderStatus.PRODUCT_RESERVING.name()
            );

            log.info("Order update status finished successfully for order={}", request.getOrderId());
            productClient.reserve(
                    request.getProductId(),
                    request.getQuantity()
            );

            log.info("Reserve product finished successfully for order={}", request.getOrderId());


            productReserved = true;

            orderClient.updateStatus(
                    request.getOrderId(),
                    OrderStatus.PRODUCT_RESERVED.name()
            );

            orderClient.updateStatus(
                    request.getOrderId(),
                    OrderStatus.PAYMENT_PROCESSING.name()
            );

            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setOrderId(request.getOrderId());
            paymentRequest.setCustomerId(request.getCustomerId());
            paymentRequest.setAmount(calculateAmount());

            var paymentResponse = paymentClient.pay(paymentRequest);

            if (paymentResponse.getStatus() == PaymentStatus.SUCCESS) {

                orderClient.updateStatus(
                        request.getOrderId(),
                        OrderStatus.COMPLETED.name()
                );

                orderEventProducer.sendOrderCompletedEvent(
                        request.getOrderId(),
                        request.getProductId(),
                        request.getQuantity().intValue(),
                        request.getPrice(),
                        request.getEmail()
                );

            } else {
                rollback(request, productReserved);
            }

        } catch (Exception e) {
            rollback(request, productReserved);
        }
    }

    public void failFlow(SagaRequest request) {

        orderClient.updateStatus(request.getOrderId(), String.valueOf(OrderStatus.FAILED));

        try {
            productClient.release(request.getProductId(), request.getQuantity());
        } catch (Exception ignored) {}
    }

    public BigDecimal calculateAmount() {
        return BigDecimal.valueOf(100);
    }

    public void rollback(SagaRequest request, boolean productReserved) {

        if (productReserved) {
            try {
                productClient.release(
                        request.getProductId(),
                        request.getQuantity()
                );


            } catch (Exception ex) {
            }
        }

        orderClient.updateStatus(
                request.getOrderId(),
                OrderStatus.FAILED.name()
        );

        orderEventProducer.sendOrderFailedEvent(
                request.getOrderId(),
                request.getProductId(),
                request.getQuantity().intValue(),
                request.getPrice(),
                request.getEmail()
        );
    }
}