package com.booking.cinema.controller;

import com.booking.cinema.dto.request.LoginRequestDTO;
import com.booking.cinema.dto.request.RegisterRequestDTO;
import com.booking.cinema.dto.response.LoginResponseDTO;
import com.booking.cinema.dto.response.UserResponseDTO;
import com.booking.cinema.model.Users;
import com.booking.cinema.security.jwt.JwtProvider;
import com.booking.cinema.service.UserService;

import com.booking.cinema.service.auth.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public AccountController(UserService userService,  JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        UserResponseDTO response = userService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO user) {
        Users login = userService.login(user);

        String token = jwtProvider.generateToken(login);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(3600)
                .build();

        LoginResponseDTO response = new LoginResponseDTO("Login realizado com sucesso! Bem-vindo.");

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(@AuthenticationPrincipal CustomUserDetails userLogged) {
        Users user = userLogged.getUser();
        UserResponseDTO response = new UserResponseDTO(user.getId(), user.getName(), user.getEmail());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }


}
