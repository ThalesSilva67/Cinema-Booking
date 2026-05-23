package com.booking.cinema.service;

import com.booking.cinema.dto.request.RegisterRequestDTO;
import com.booking.cinema.dto.response.LoginResponseDTO;
import com.booking.cinema.dto.response.UserResponseDTO;
import com.booking.cinema.model.Users;
import com.booking.cinema.model.Role;
import com.booking.cinema.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO registerStandardUser(RegisterRequestDTO request) {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            throw new IllegalStateException("Email  already exists");
        });

        Users user = new Users();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(Role.STANDARD);

        Users saved = userRepository.save(user);

        return new UserResponseDTO(saved.getId(), saved.getName(), saved.getEmail());
    }

    public UserResponseDTO registerAdmUser(RegisterRequestDTO request) {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            throw new IllegalArgumentException("Email already exists");
        });

        Users user = new Users();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(Role.ADMINISTRADOR);

        Users saved = userRepository.save(user);

        return new UserResponseDTO(saved.getId(), saved.getName(), saved.getEmail());
    }

    public LoginResponseDTO loginUser(String email, String password) {
        Users user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if(!password.equals(user.getPassword())) {
            throw new  IllegalArgumentException("Invalid credentials");
        }

        return new LoginResponseDTO("Login realizado com sucesso!");
    }

    public List<Users> findAll() {
        List<Users> list = userRepository.findAll();

        if (list.isEmpty()) return new ArrayList<>();

        return list;
    }

    public Users findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Users not found with ID: " + id));
    }

    public void delete(Long id) {
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("User not found");
        }

        userRepository.deleteById(id);
    }

    public UserResponseDTO updateStandard(Long id, RegisterRequestDTO user) {
        Users oldUsers =  userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Users not found"));

        oldUsers.setName(user.name());
        oldUsers.setEmail(user.email());
        oldUsers.setPassword(user.password());

        Users update = userRepository.save(oldUsers);

        return new UserResponseDTO(update.getId(), update.getName(), update.getEmail());
    }

    public UserResponseDTO updateAdm(Long id, RegisterRequestDTO user) {
        Users oldUsers =  userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Users not found"));

        oldUsers.setName(user.name());
        oldUsers.setEmail(user.email());
        oldUsers.setPassword(user.password());

        Users update = userRepository.save(oldUsers);

        return new UserResponseDTO(update.getId(), update.getName(), update.getEmail());
    }

}
