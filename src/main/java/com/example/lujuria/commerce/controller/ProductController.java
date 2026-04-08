package com.example.lujuria.commerce.controller;

import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.commerce.dto.ProductRequest;
import com.example.lujuria.commerce.dto.ProductResponse;
import com.example.lujuria.commerce.entity.ProductType;
import com.example.lujuria.commerce.service.CommerceService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final CommerceService commerceService;

    public ProductController(CommerceService commerceService) {
        this.commerceService = commerceService;
    }

    @GetMapping
    public List<ProductResponse> listProducts(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) ProductType type
    ) {
        return commerceService.listProducts(category, type);
    }

    @PostMapping
    public ProductResponse createProduct(
        @AuthenticationPrincipal AppUserPrincipal principal,
        @Valid @RequestBody ProductRequest request
    ) {
        return commerceService.createProduct(principal, request);
    }
}
