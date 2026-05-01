package com.example.orderorchestrator.service.impl;

import com.example.orderorchestrator.enums.OrderStatus;
import com.example.orderorchestrator.enums.PaymentStatus;
import com.example.orderorchestrator.event.OrderEventProducer;
import com.example.orderorchestrator.model.dto.request.PaymentRequest;
import com.example.orderorchestrator.model.dto.request.ReserveRequest;
import com.example.orderorchestrator.model.dto.request.SagaRequest;
import com.example.orderorchestrator.service.SagaService;
import com.example.orderorchestrator.client.OrderClient;
import com.example.orderorchestrator.client.PaymentClient;
import com.example.orderorchestrator.client.ProductClient;
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

        log.info("SAGA STARTED for orderId={}", request.getOrderId());

        try {
            log.info("Updating order status to PRODUCT_RESERVING for orderId={}", request.getOrderId());
            orderClient.updateStatus(
                    request.getOrderId(),
                    OrderStatus.PRODUCT_RESERVING
            );
            log.info("Order update status finished successfully for order={}", request.getOrderId());

            log.info("Reserving product. productId={}, quantity={}, orderId={}",
                    request.getProductId(), request.getQuantity(), request.getOrderId());
            ReserveRequest reserveRequest = new ReserveRequest();
            reserveRequest.setProductId(request.getProductId());
            reserveRequest.setQuantity(request.getQuantity());

            productClient.reserve(reserveRequest);
            log.info("Reserve product finished successfully for order={}", request.getOrderId());

            productReserved = true;

            log.info("Updating order status to PRODUCT_RESERVED for orderId={}", request.getOrderId());
            orderClient.updateStatus(
                    request.getOrderId(),
                    OrderStatus.PRODUCT_RESERVED
            );

            log.info("Updating order status to PAYMENT_PROCESSING for orderId={}", request.getOrderId());
            orderClient.updateStatus(
                    request.getOrderId(),
                    OrderStatus.PAYMENT_PROCESSING
            );

            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setOrderId(request.getOrderId());
            paymentRequest.setCustomerId(request.getCustomerId());
            paymentRequest.setAmount(calculateAmount());

            log.info("Processing payment for orderId={}, amount={}",
                    request.getOrderId(), paymentRequest.getAmount());

            var paymentResponse = paymentClient.pay(paymentRequest);

            log.info("Payment response received for orderId={}, status={}",
                    request.getOrderId(), paymentResponse.getStatus());

            if (paymentResponse.getStatus() == PaymentStatus.SUCCESS) {

                log.info("Payment SUCCESS for orderId={}", request.getOrderId());

                orderClient.updateStatus(
                        request.getOrderId(),
                        OrderStatus.COMPLETED
                );

                log.info("Order marked as COMPLETED for orderId={}", request.getOrderId());

                orderEventProducer.sendOrderCompletedEvent(
                        request.getOrderId(),
                        request.getProductId(),
                        request.getQuantity().intValue(),
                        request.getPrice(),
                        request.getEmail(),
                        request.getCustomerId()
                );

                log.info("OrderCompletedEvent sent for orderId={}", request.getOrderId());

            } else {
                log.warn("Payment FAILED for orderId={}, triggering rollback", request.getOrderId());
                rollback(request, productReserved);
            }

        } catch (Exception e) {
            log.error("SAGA FAILED for orderId={}, error={}", request.getOrderId(), e.getMessage(), e);
            rollback(request, productReserved);
        }
    }
    public void failFlow(SagaRequest request) {

        orderClient.updateStatus(request.getOrderId(), OrderStatus.FAILED);

        try {
            productClient.release(request.getProductId(), request.getQuantity());
        } catch (Exception ignored) {}
    }

    public BigDecimal calculateAmount() {
        return BigDecimal.valueOf(100);
    }

    public void rollback(SagaRequest request, boolean productReserved) {

        log.warn("ROLLBACK STARTED for orderId={}", request.getOrderId());

        if (productReserved) {
            try {
                log.info("Releasing product for orderId={}", request.getOrderId());

                productClient.release(
                        request.getProductId(),
                        request.getQuantity()
                );

                log.info("Product released successfully for orderId={}", request.getOrderId());

            } catch (Exception ex) {
                log.error("Failed to release product for orderId={}, error={}",
                        request.getOrderId(), ex.getMessage(), ex);
            }
        }

        log.info("Updating order status to FAILED for orderId={}", request.getOrderId());
        orderClient.updateStatus(
                request.getOrderId(),
                OrderStatus.FAILED
        );

        orderEventProducer.sendOrderFailedEvent(
                request.getOrderId(),
                request.getProductId(),
                request.getQuantity().intValue(),
                request.getPrice(),
                request.getEmail(),
                request.getCustomerId()
        );

        log.info("OrderFailedEvent sent for orderId={}", request.getOrderId());
    }
}