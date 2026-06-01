package com.booking.cinema.security;

import com.booking.cinema.security.jwt.JwtAuthenticationEntryPoint;
import com.booking.cinema.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter authenticationFilter;
    private final JwtAuthenticationEntryPoint authEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter authenticationFilter,  JwtAuthenticationEntryPoint authEntryPoint) {
        this.authenticationFilter = authenticationFilter;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                            "/api/account/register",
                            "/api/account/login",
                            "/api/payments/webhook",
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html"
                    ).permitAll();

                    auth.requestMatchers(HttpMethod.POST, "/api/movies").hasRole("ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.POST, "/api/rooms").hasRole("ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.POST, "/api/sessions").hasRole("ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.POST, "/api/users/admin").hasRole("ADMINISTRADOR");


                    auth.requestMatchers(HttpMethod.PUT, "/api/movies/**").hasRole("ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.PUT, "/api/rooms/**").hasRole("ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.PUT, "/api/sessions/**").hasRole("ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.PUT, "/api/users/admin/**").hasRole("ADMINISTRADOR");

                    auth.requestMatchers(HttpMethod.DELETE, "/api/movies/**").hasRole("ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/rooms/**").hasRole("ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/sessions/**").hasRole("ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/users/admin/**").hasRole("ADMINISTRADOR");

                    auth.anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
