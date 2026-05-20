package com.booking.cinema.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Booking booking;

    @Column(precision = 10, scale = 2, nullable = false)
    @Positive
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(length = 50, nullable = false)
    private String paymentMethod;

    @Column(length = 50, nullable = false)
    private String externalTransactionId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Payment() {}

    public Payment(Long id, Booking booking, BigDecimal amount, PaymentStatus status, String paymentMethod, String externalTransactionId, LocalDateTime createdAt) {
        this.id = id;
        this.booking = booking;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.externalTransactionId = externalTransactionId;
        this.createdAt = createdAt;
    }

    public Payment(Booking booking, BigDecimal amount, PaymentStatus status, String paymentMethod, String externalTransactionId) {
        this.booking = booking;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.externalTransactionId = externalTransactionId;
    }

    public Long getId() {
        return id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getExternalTransactionId() {
        return externalTransactionId;
    }

    public void setExternalTransactionId(String externalTransactionId) {
        this.externalTransactionId = externalTransactionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
