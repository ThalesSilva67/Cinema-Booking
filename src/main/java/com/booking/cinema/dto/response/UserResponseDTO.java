package com.booking.cinema.dto.response;

import com.booking.cinema.model.Role;

public record UserResponseDTO(Long id, String name, String email, Role role) {
}
