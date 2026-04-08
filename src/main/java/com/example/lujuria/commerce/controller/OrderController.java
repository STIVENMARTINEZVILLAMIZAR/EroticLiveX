package com.example.lujuria.commerce.controller;

import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.commerce.dto.CreateOrderRequest;
import com.example.lujuria.commerce.dto.OrderResponse;
import com.example.lujuria.commerce.service.CommerceService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final CommerceService commerceService;

    public OrderController(CommerceService commerceService) {
        this.commerceService = commerceService;
    }

    @PostMapping
    public OrderResponse createOrder(
        @AuthenticationPrincipal AppUserPrincipal principal,
        @Valid @RequestBody CreateOrderRequest request
    ) {
        return commerceService.createOrder(principal, request);
    }

    @GetMapping("/me")
    public List<OrderResponse> myOrders(@AuthenticationPrincipal AppUserPrincipal principal) {
        return commerceService.myOrders(principal);
    }
}
