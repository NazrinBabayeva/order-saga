package com.example.orderorchestrator.client;

import com.example.orderorchestrator.model.dto.request.ReserveRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-ms", url = "http://localhost:8082/product-ms/api")
public interface ProductClient {

    @PostMapping("/v1/products/reserve")
    void reserve(@RequestBody ReserveRequest request);

    @PostMapping("/v1/products/{productId}/release")
    void release(@PathVariable("productId") Long productId,
                 @RequestParam("quantity") Long quantity);

}
