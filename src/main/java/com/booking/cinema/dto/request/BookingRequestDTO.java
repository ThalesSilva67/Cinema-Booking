package com.booking.cinema.dto.request;

import com.booking.cinema.model.TypeTicket;

public record BookingRequestDTO(Long sessionId, Long userId, String seatLabel, TypeTicket ticket) {
}
