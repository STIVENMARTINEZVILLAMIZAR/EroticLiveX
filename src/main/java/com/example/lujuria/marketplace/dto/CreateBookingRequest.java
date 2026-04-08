package com.example.lujuria.marketplace.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public record CreateBookingRequest(
    @NotNull @Future Instant scheduledAt,
    @Size(max = 1000) String notes
) {
}
