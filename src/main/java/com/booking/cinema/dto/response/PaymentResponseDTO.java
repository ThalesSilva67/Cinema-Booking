package com.booking.cinema.dto.response;

import java.math.BigDecimal;

public record PaymentResponseDTO(String externalTransactionId, BigDecimal amount, String url) {
}
