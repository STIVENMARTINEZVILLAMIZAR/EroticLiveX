package com.example.lujuria.commerce.dto;

import com.example.lujuria.commerce.entity.Product;
import java.math.BigDecimal;

public record ProductResponse(
    Long id,
    Long creatorId,
    String creatorDisplayName,
    String category,
    String name,
    String description,
    String type,
    BigDecimal price,
    Integer inventory,
    boolean active,
    String imageUrl
) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getCreator().getId(),
            product.getCreator().getDisplayName(),
            product.getCategory() != null ? product.getCategory().getName() : null,
            product.getName(),
            product.getDescription(),
            product.getType().name(),
            product.getPrice(),
            product.getInventory(),
            product.isActive(),
            product.getImageUrl()
        );
    }
}
