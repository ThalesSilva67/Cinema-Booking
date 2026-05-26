package com.booking.cinema.repository;

import com.booking.cinema.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment>findByExternalTransactionId(String externalId);
    Optional<Payment>findByBookingId(Long bookingId);
}
