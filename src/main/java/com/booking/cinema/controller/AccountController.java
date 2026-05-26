package com.booking.cinema.controller;

import com.booking.cinema.dto.request.LoginRequestDTO;
import com.booking.cinema.dto.request.RegisterRequestDTO;
import com.booking.cinema.dto.response.LoginResponseDTO;
import com.booking.cinema.dto.response.UserResponseDTO;
import com.booking.cinema.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        UserResponseDTO response = userService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO user) {
        LoginResponseDTO response = userService.login(user);

        return ResponseEntity.ok(response);
    }


}
