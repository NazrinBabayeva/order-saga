package com.example.orderorchestrator.service;

import com.example.orderorchestrator.model.dto.request.SagaRequest;

public interface SagaService {

    void startSaga(SagaRequest request);
    void failFlow(SagaRequest request);
    void rollback(SagaRequest request, boolean productReserved);
    java.math.BigDecimal calculateAmount();
}