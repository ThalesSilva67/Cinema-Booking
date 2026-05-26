package com.booking.cinema.service;

import com.booking.cinema.dto.request.BookingRequestId;
import com.booking.cinema.dto.request.WebHookRequestDTO;
import com.booking.cinema.dto.response.BookingResponseDTO;
import com.booking.cinema.dto.response.PaymentResponseDTO;
import com.booking.cinema.exception.BusinessRuleException;
import com.booking.cinema.model.Booking;
import com.booking.cinema.model.BookingState;
import com.booking.cinema.model.Payment;
import com.booking.cinema.model.PaymentStatus;
import com.booking.cinema.repository.BookingRepository;
import com.booking.cinema.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    public PaymentService(PaymentRepository paymentRepository, BookingService bookingService,  BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public PaymentResponseDTO createPayment(BookingRequestId bg) {
        Booking bookingResponse = bookingRepository.findById(bg.bookingId()).orElseThrow(() -> new BusinessRuleException("Reserva não encontrada"));

        if(bookingResponse.getState() == BookingState.CANCELED || bookingResponse.getState() == BookingState.APPROVED || bookingResponse.getState() == BookingState.EXPIRED) {
            throw new BusinessRuleException("Reserva ja encerrada ou aprovada");
        }

        Optional<Payment> existingPayment = paymentRepository.findByBookingId(bg.bookingId());
        if(existingPayment.isPresent() && existingPayment.get().getStatus() == PaymentStatus.PENDING) {
            throw new BusinessRuleException("Checkout já iniciado");
        }

        Payment payment = existingPayment.orElse(new Payment());

        payment.setBooking(bookingResponse);
        payment.setPaymentMethod("PIX");
        payment.setAmount(bookingResponse.getPrice());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setExternalTransactionId(UUID.randomUUID().toString());

        Payment saved = paymentRepository.save(payment);

        return new PaymentResponseDTO(saved.getExternalTransactionId(), saved.getAmount());
    }

    @Transactional
    public void processWebhook(WebHookRequestDTO webHookRequestDTO) {
        Payment payment = paymentRepository.findByExternalTransactionId(webHookRequestDTO.externalTransactionId()).orElseThrow(() -> new BusinessRuleException("ExternalTransactionId invalido"));

        if (webHookRequestDTO.amount().compareTo(payment.getAmount()) != 0) {
            throw new BusinessRuleException("Valor do webhook não bate com o valor da cobrança! Possível fraude.");
        }

        String gatewayStatus = webHookRequestDTO.status().toUpperCase();

        switch (gatewayStatus) {
            case "APPROVED":
            case "SUCCEEDED":
                payment.setStatus(PaymentStatus.APPROVED);
                bookingService.confirmBooking(payment.getBooking().getId());
                break;

            case "FAILED":
            case "REJECTED":
                payment.setStatus(PaymentStatus.FAILED);
                bookingService.cancelBooking(payment.getBooking().getId());
                break;

            case "REFUNDED":
                payment.setStatus(PaymentStatus.REFUNDED);
                bookingService.cancelBooking(payment.getBooking().getId());
                break;

            default:
                System.out.println("Status de webhook não mapeado ou ignorado: " + gatewayStatus);
                break;
        }
    }
}
