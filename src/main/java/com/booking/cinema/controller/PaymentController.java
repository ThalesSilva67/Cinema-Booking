package com.booking.cinema.controller;

import com.booking.cinema.dto.request.BookingRequestId;
import com.booking.cinema.dto.request.WebHookRequestDTO;
import com.booking.cinema.dto.response.PaymentResponseDTO;
import com.booking.cinema.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> create(@Valid @RequestBody BookingRequestId request) {
        PaymentResponseDTO response = paymentService.createPayment(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@Valid @RequestBody WebHookRequestDTO request) {
        paymentService.processWebhook(request);

        return ResponseEntity.ok().build();
    }
}
