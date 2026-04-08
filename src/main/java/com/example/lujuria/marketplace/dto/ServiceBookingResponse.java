package com.example.lujuria.marketplace.dto;

import com.example.lujuria.marketplace.entity.ServiceBooking;
import java.time.Instant;

public record ServiceBookingResponse(
    Long id,
    Long serviceId,
    String serviceTitle,
    Long customerId,
    Long creatorId,
    String creatorDisplayName,
    Instant scheduledAt,
    String status,
    String notes,
    String internalNotes
) {

    public static ServiceBookingResponse from(ServiceBooking booking) {
        return new ServiceBookingResponse(
            booking.getId(),
            booking.getServiceOffering().getId(),
            booking.getServiceOffering().getTitle(),
            booking.getCustomer().getId(),
            booking.getCreator().getId(),
            booking.getCreator().getDisplayName(),
            booking.getScheduledAt(),
            booking.getStatus().name(),
            booking.getNotes(),
            booking.getInternalNotes()
        );
    }
}
