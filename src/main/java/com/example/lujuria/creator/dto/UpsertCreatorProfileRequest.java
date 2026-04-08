package com.example.lujuria.creator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpsertCreatorProfileRequest(
    @NotBlank @Size(max = 80) String displayName,
    @Size(max = 120) String headline,
    @Size(max = 2000) String description,
    @Size(max = 255) String avatarUrl
) {
}
