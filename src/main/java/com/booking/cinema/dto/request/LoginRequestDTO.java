package com.booking.cinema.dto.request;

import com.booking.cinema.model.Role;

public record LoginRequestDTO(String email, String password, Role role) {
}
