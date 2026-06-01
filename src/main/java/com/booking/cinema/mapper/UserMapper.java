package com.booking.cinema.mapper;

import com.booking.cinema.dto.response.LoginResponseDTO;
import com.booking.cinema.dto.response.UserResponseDTO;
import com.booking.cinema.model.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDTO toUserResponseDTO(Users user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    public Users toUsers(UserResponseDTO userResponseDTO) {
        return new Users(userResponseDTO.id(), userResponseDTO.name(), userResponseDTO.email());
    }

    public LoginResponseDTO toLoginResponseDTO(String message) {
        return new LoginResponseDTO(message);
    }
}
