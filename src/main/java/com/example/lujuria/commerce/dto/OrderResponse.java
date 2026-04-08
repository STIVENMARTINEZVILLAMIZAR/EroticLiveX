package com.example.lujuria.commerce.dto;

import com.example.lujuria.commerce.entity.CustomerOrder;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
    Long id,
    String status,
    BigDecimal totalAmount,
    String currency,
    String paymentStatus,
    String paymentReference,
    Instant createdAt,
    List<OrderItemResponse> items
) {

    public static OrderResponse from(CustomerOrder order) {
        return new OrderResponse(
            order.getId(),
            order.getStatus().name(),
            order.getTotalAmount(),
            order.getCurrency(),
            order.getPayment() != null ? order.getPayment().getStatus().name() : null,
            order.getPayment() != null ? order.getPayment().getExternalReference() : null,
            order.getCreatedAt(),
            order.getItems().stream().map(OrderItemResponse::from).toList()
        );
    }
}
