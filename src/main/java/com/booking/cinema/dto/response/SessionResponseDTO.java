package com.booking.cinema.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SessionResponseDTO(Long id, Long movieId, Long roomId, LocalDateTime startTime, BigDecimal price) {
}
