package com.example.lujuria.stream.dto;

import com.example.lujuria.stream.entity.StreamStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public record LiveStreamRequest(
    @NotBlank @Size(max = 150) String title,
    @Size(max = 2000) String description,
    @NotBlank String categorySlug,
    List<String> tagSlugs,
    @NotBlank @Size(max = 255) String playbackUrl,
    @Size(max = 255) String coverImageUrl,
    @NotNull @DecimalMin("0.00") BigDecimal accessPrice,
    StreamStatus status
) {
}
