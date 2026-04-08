package com.example.lujuria.marketplace.dto;

import com.example.lujuria.marketplace.entity.ServiceMode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ServiceOfferingRequest(
    @NotBlank @Size(max = 150) String title,
    @Size(max = 2000) String description,
    @NotBlank String categorySlug,
    @NotNull ServiceMode mode,
    @NotNull @DecimalMin("0.00") BigDecimal price,
    @NotNull Integer durationMinutes,
    @Size(max = 255) String locationLabel,
    @Size(max = 255) String meetingLink
) {
}
