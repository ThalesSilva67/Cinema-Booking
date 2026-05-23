package com.booking.cinema.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SessionRequestDTO (Long movieId, Long roomId, LocalDateTime startTime, BigDecimal price){
}
