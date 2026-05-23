package com.booking.cinema.dto.response;

import com.booking.cinema.model.BookingState;
import com.booking.cinema.model.TypeTicket;

import java.math.BigDecimal;

public record BookingResponseDTO(Long id, Long session_id, Long user_id, String seatLabel, BigDecimal price, BookingState state, TypeTicket ticket) {
}
