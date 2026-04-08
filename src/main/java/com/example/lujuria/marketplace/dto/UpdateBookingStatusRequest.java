package com.example.lujuria.marketplace.dto;

import com.example.lujuria.marketplace.entity.BookingStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateBookingStatusRequest(
    @NotNull BookingStatus status,
    @Size(max = 1000) String internalNotes
) {
}
