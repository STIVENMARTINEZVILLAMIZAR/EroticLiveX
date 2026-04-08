package com.example.lujuria.commerce.dto;

import com.example.lujuria.commerce.entity.OrderItem;
import java.math.BigDecimal;

public record OrderItemResponse(
    Long productId,
    String productName,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {

    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
            item.getProduct().getId(),
            item.getProductName(),
            item.getQuantity(),
            item.getUnitPrice(),
            item.getSubtotal()
        );
    }
}
