package com.example.lujuria.marketplace.service;

import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.catalog.entity.Category;
import com.example.lujuria.catalog.entity.CategoryType;
import com.example.lujuria.catalog.repository.CategoryRepository;
import com.example.lujuria.common.BusinessRuleException;
import com.example.lujuria.common.ResourceNotFoundException;
import com.example.lujuria.creator.entity.CreatorProfile;
import com.example.lujuria.creator.service.CreatorService;
import com.example.lujuria.marketplace.dto.CreateBookingRequest;
import com.example.lujuria.marketplace.dto.ServiceBookingResponse;
import com.example.lujuria.marketplace.dto.ServiceOfferingRequest;
import com.example.lujuria.marketplace.dto.ServiceOfferingResponse;
import com.example.lujuria.marketplace.dto.UpdateBookingStatusRequest;
import com.example.lujuria.marketplace.entity.BookingStatus;
import com.example.lujuria.marketplace.entity.ServiceBooking;
import com.example.lujuria.marketplace.entity.ServiceMode;
import com.example.lujuria.marketplace.entity.ServiceOffering;
import com.example.lujuria.marketplace.repository.ServiceBookingRepository;
import com.example.lujuria.marketplace.repository.ServiceOfferingRepository;
import com.example.lujuria.user.entity.AppUser;
import com.example.lujuria.user.service.UserService;
import java.util.List;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarketplaceService {

    private final ServiceOfferingRepository serviceOfferingRepository;
    private final ServiceBookingRepository serviceBookingRepository;
    private final CategoryRepository categoryRepository;
    private final CreatorService creatorService;
    private final UserService userService;

    public MarketplaceService(
        ServiceOfferingRepository serviceOfferingRepository,
        ServiceBookingRepository serviceBookingRepository,
        CategoryRepository categoryRepository,
        CreatorService creatorService,
        UserService userService
    ) {
        this.serviceOfferingRepository = serviceOfferingRepository;
        this.serviceBookingRepository = serviceBookingRepository;
        this.categoryRepository = categoryRepository;
        this.creatorService = creatorService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<ServiceOfferingResponse> listServices(String country, String category, ServiceMode mode) {
        Specification<ServiceOffering> specification = Specification.allOf(
            ServiceOfferingSpecifications.isActive(),
            ServiceOfferingSpecifications.hasCountry(country),
            ServiceOfferingSpecifications.hasCategorySlug(category),
            ServiceOfferingSpecifications.hasMode(mode)
        );

        return serviceOfferingRepository.findAll(specification).stream()
            .map(ServiceOfferingResponse::from)
            .toList();
    }

    @Transactional
    public ServiceOfferingResponse createService(AppUserPrincipal principal, ServiceOfferingRequest request) {
        CreatorProfile creator = creatorService.getRequiredCreatorByUserId(principal.getUserId());
        Category category = categoryRepository.findBySlug(request.categorySlug().trim().toLowerCase(Locale.ROOT))
            .filter(existing -> existing.getType() == CategoryType.SERVICE)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria de servicios no encontrada."));

        ServiceOffering offering = new ServiceOffering();
        offering.setCreator(creator);
        offering.setCategory(category);
        offering.setTitle(request.title().trim());
        offering.setDescription(request.description());
        offering.setMode(request.mode());
        offering.setPrice(request.price());
        offering.setDurationMinutes(request.durationMinutes());
        offering.setLocationLabel(request.locationLabel());
        offering.setMeetingLink(request.meetingLink());

        ServiceOffering saved = serviceOfferingRepository.save(offering);
        return ServiceOfferingResponse.from(saved);
    }

    @Transactional
    public ServiceBookingResponse createBooking(
        AppUserPrincipal principal,
        Long serviceId,
        CreateBookingRequest request
    ) {
        AppUser user = userService.getRequiredUser(principal.getUserId());
        ServiceOffering offering = serviceOfferingRepository.findById(serviceId)
            .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado."));

        if (!offering.isActive()) {
            throw new BusinessRuleException("El servicio no esta disponible.");
        }
        if (offering.getCreator().getUser().getId().equals(user.getId())) {
            throw new BusinessRuleException("No puedes reservar tu propio servicio.");
        }

        ServiceBooking booking = new ServiceBooking();
        booking.setServiceOffering(offering);
        booking.setCustomer(user);
        booking.setCreator(offering.getCreator());
        booking.setScheduledAt(request.scheduledAt());
        booking.setNotes(request.notes());

        return ServiceBookingResponse.from(serviceBookingRepository.save(booking));
    }

    @Transactional(readOnly = true)
    public List<ServiceBookingResponse> listMyBookings(AppUserPrincipal principal) {
        return serviceBookingRepository.findVisibleBookings(principal.getUserId()).stream()
            .map(ServiceBookingResponse::from)
            .toList();
    }

    @Transactional
    public ServiceBookingResponse updateBookingStatus(
        AppUserPrincipal principal,
        Long bookingId,
        UpdateBookingStatusRequest request
    ) {
        CreatorProfile creator = creatorService.getRequiredCreatorByUserId(principal.getUserId());
        ServiceBooking booking = serviceBookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada."));

        if (!booking.getCreator().getId().equals(creator.getId())) {
            throw new BusinessRuleException("Solo el creador propietario puede cambiar el estado de la reserva.");
        }
        if (booking.getStatus() == BookingStatus.COMPLETED && request.status() != BookingStatus.COMPLETED) {
            throw new BusinessRuleException("Una reserva completada no debe volver a estados previos.");
        }

        booking.setStatus(request.status());
        booking.setInternalNotes(request.internalNotes());
        return ServiceBookingResponse.from(booking);
    }
}
