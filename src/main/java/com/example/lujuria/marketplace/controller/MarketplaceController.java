package com.example.lujuria.marketplace.controller;

import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.marketplace.dto.CreateBookingRequest;
import com.example.lujuria.marketplace.dto.ServiceBookingResponse;
import com.example.lujuria.marketplace.dto.ServiceOfferingRequest;
import com.example.lujuria.marketplace.dto.ServiceOfferingResponse;
import com.example.lujuria.marketplace.dto.UpdateBookingStatusRequest;
import com.example.lujuria.marketplace.entity.ServiceMode;
import com.example.lujuria.marketplace.service.MarketplaceService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/services")
public class MarketplaceController {

    private final MarketplaceService marketplaceService;

    public MarketplaceController(MarketplaceService marketplaceService) {
        this.marketplaceService = marketplaceService;
    }

    @GetMapping
    public List<ServiceOfferingResponse> listServices(
        @RequestParam(required = false) String country,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) ServiceMode mode
    ) {
        return marketplaceService.listServices(country, category, mode);
    }

    @PostMapping
    public ServiceOfferingResponse createService(
        @AuthenticationPrincipal AppUserPrincipal principal,
        @Valid @RequestBody ServiceOfferingRequest request
    ) {
        return marketplaceService.createService(principal, request);
    }

    @PostMapping("/{serviceId}/bookings")
    public ServiceBookingResponse createBooking(
        @AuthenticationPrincipal AppUserPrincipal principal,
        @PathVariable Long serviceId,
        @Valid @RequestBody CreateBookingRequest request
    ) {
        return marketplaceService.createBooking(principal, serviceId, request);
    }

    @GetMapping("/bookings/me")
    public List<ServiceBookingResponse> myBookings(@AuthenticationPrincipal AppUserPrincipal principal) {
        return marketplaceService.listMyBookings(principal);
    }

    @PatchMapping("/bookings/{bookingId}")
    public ServiceBookingResponse updateBookingStatus(
        @AuthenticationPrincipal AppUserPrincipal principal,
        @PathVariable Long bookingId,
        @Valid @RequestBody UpdateBookingStatusRequest request
    ) {
        return marketplaceService.updateBookingStatus(principal, bookingId, request);
    }
}
