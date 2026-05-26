package com.booking.cinema.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WebHookRequestDTO(String externalTransactionId, String status, LocalDateTime timestamp, BigDecimal amount) {
}
