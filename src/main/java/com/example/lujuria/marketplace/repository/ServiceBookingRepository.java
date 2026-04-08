package com.example.lujuria.marketplace.repository;

import com.example.lujuria.marketplace.entity.ServiceBooking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceBookingRepository extends JpaRepository<ServiceBooking, Long> {

    @Query("""
        select booking
        from ServiceBooking booking
        where booking.customer.id = :userId or booking.creator.user.id = :userId
        order by booking.scheduledAt desc
        """)
    List<ServiceBooking> findVisibleBookings(Long userId);
}
