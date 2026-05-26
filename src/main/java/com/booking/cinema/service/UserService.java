package com.booking.cinema.service;

import com.booking.cinema.dto.request.LoginRequestDTO;
import com.booking.cinema.dto.request.RegisterRequestDTO;
import com.booking.cinema.dto.response.LoginResponseDTO;
import com.booking.cinema.dto.response.UserResponseDTO;
import com.booking.cinema.exception.DuplicateEmailException;
import com.booking.cinema.exception.InvalidCredentialsException;
import com.booking.cinema.mapper.UserMapper;
import com.booking.cinema.model.Users;
import com.booking.cinema.model.Role;
import com.booking.cinema.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository,  UserMapper userMapper,  PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    public UserResponseDTO register(RegisterRequestDTO request) {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            throw new DuplicateEmailException("Email already exists");
        });

        Users user = new Users();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setRole(Role.STANDARD);

        Users saved = userRepository.save(user);

        return userMapper.toUserResponseDTO(saved);
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        Users user = userRepository.findByEmail(request.email()).orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
        if(!encoder.matches(request.password(), user.getPassword())) throw new InvalidCredentialsException("Invalid credentials");

        return userMapper.toLoginResponseDTO("Login realizado com sucesso!");
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream().map(userMapper::toUserResponseDTO).toList();
    }

    public UserResponseDTO findById(Long id) {
        Users user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toUserResponseDTO(user);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public UserResponseDTO update(Long id, RegisterRequestDTO user) {
        Users oldUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Users not found"));

        Optional<Users> existingUser = userRepository.findByEmail(user.email());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
            throw new DuplicateEmailException("Email already exists");
        }

        oldUser.setName(user.name());
        oldUser.setEmail(user.email());
        oldUser.setPassword(user.password());

        Users updated = userRepository.save(oldUser);

        return userMapper.toUserResponseDTO(updated);
    }

}
