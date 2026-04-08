package com.example.lujuria.commerce.dto;

import com.example.lujuria.commerce.entity.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductRequest(
    @NotBlank @Size(max = 150) String name,
    @Size(max = 2000) String description,
    @NotBlank String categorySlug,
    @NotNull ProductType type,
    @NotNull @DecimalMin("0.00") BigDecimal price,
    @NotNull Integer inventory,
    @Size(max = 255) String imageUrl
) {
}
