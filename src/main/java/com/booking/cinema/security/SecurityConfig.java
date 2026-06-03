package com.booking.cinema.security;

import com.booking.cinema.component.CustomAccessDeniedHandler;
import com.booking.cinema.component.JwtAuthenticationEntryPoint;
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
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter authenticationFilter,  JwtAuthenticationEntryPoint authEntryPoint,  CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.authenticationFilter = authenticationFilter;
        this.authEntryPoint = authEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
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

                    auth.requestMatchers(HttpMethod.GET, "/api/movies/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/rooms/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/sessions/**").permitAll();

                    auth.requestMatchers(HttpMethod.POST, "/api/movies").hasAuthority("ROLE_ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.POST, "/api/rooms").hasAuthority("ROLE_ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.POST, "/api/sessions").hasAuthority("ROLE_ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.POST, "/api/users/admin").hasAuthority("ROLE_ADMINISTRADOR");


                    auth.requestMatchers(HttpMethod.PUT, "/api/movies/**").hasAuthority("ROLE_ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.PUT, "/api/rooms/**").hasAuthority("ROLE_ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.PUT, "/api/sessions/**").hasAuthority("ROLE_ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.PUT, "/api/users/admin/**").hasAuthority("ROLE_ADMINISTRADOR");

                    auth.requestMatchers(HttpMethod.DELETE, "/api/movies/**").hasAuthority("ROLE_ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/rooms/**").hasAuthority("ROLE_ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/sessions/**").hasAuthority("ROLE_ADMINISTRADOR");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/users/admin/**").hasAuthority("ROLE_ADMINISTRADOR");

                    auth.anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint).accessDeniedHandler(customAccessDeniedHandler))
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
