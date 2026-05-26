package com.booking.cinema.dto.response;

import java.time.LocalDateTime;

public record ErrorResponseDTO (Integer status, String message, String path, LocalDateTime timestamp) {
}
