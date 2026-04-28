package com.example.orderorchestrator.controller;

import com.example.orderorchestrator.model.dto.request.SagaRequest;
import com.example.orderorchestrator.service.SagaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/saga")
@RequiredArgsConstructor
public class SagaController {

    private final SagaService sagaService;

    @PostMapping("/start")
    public void startSaga(@RequestBody SagaRequest request) {
        sagaService.startSaga(request);
    }
}
