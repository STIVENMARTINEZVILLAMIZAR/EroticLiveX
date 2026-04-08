package com.example.lujuria.marketplace.dto;

import com.example.lujuria.marketplace.entity.ServiceOffering;
import java.math.BigDecimal;

public record ServiceOfferingResponse(
    Long id,
    Long creatorId,
    String creatorDisplayName,
    String creatorCountry,
    String category,
    String title,
    String description,
    String mode,
    BigDecimal price,
    Integer durationMinutes,
    boolean active,
    String locationLabel,
    String meetingLink
) {

    public static ServiceOfferingResponse from(ServiceOffering offering) {
        return new ServiceOfferingResponse(
            offering.getId(),
            offering.getCreator().getId(),
            offering.getCreator().getDisplayName(),
            offering.getCreator().getUser().getCountry(),
            offering.getCategory() != null ? offering.getCategory().getName() : null,
            offering.getTitle(),
            offering.getDescription(),
            offering.getMode().name(),
            offering.getPrice(),
            offering.getDurationMinutes(),
            offering.isActive(),
            offering.getLocationLabel(),
            offering.getMeetingLink()
        );
    }
}
