package com.booking.cinema.controller;

import com.booking.cinema.doc.PaymentControllerDoc;
import com.booking.cinema.dto.request.BookingRequestId;
import com.booking.cinema.dto.response.PaymentResponseDTO;
import com.booking.cinema.service.PaymentService;
import com.booking.cinema.service.external.StripeWebhookAdapter;
import com.stripe.model.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController implements PaymentControllerDoc {
    private final PaymentService paymentService;
    private final StripeWebhookAdapter stripeWebhookAdapter;

    public PaymentController(PaymentService paymentService, StripeWebhookAdapter stripeWebhookAdapter) {
        this.paymentService = paymentService;
        this.stripeWebhookAdapter = stripeWebhookAdapter;
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> create(@Valid @RequestBody BookingRequestId request) {
        PaymentResponseDTO response = paymentService.createPayment(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        stripeWebhookAdapter.processWebhookStripeEvent(payload, sigHeader);
        return ResponseEntity.ok().build();
    }
}
