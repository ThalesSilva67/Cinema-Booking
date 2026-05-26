package com.booking.cinema.model;

public enum PaymentStatus {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    FAILED("FAILED"),
    REFUNDED("REFUNDED"),
    CANCELED("CANCELED");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }
}
