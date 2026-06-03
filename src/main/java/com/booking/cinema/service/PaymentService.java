package com.booking.cinema.service;

import com.booking.cinema.dto.request.BookingRequestId;
import com.booking.cinema.dto.request.WebHookRequestDTO;
import com.booking.cinema.dto.response.PaymentResponseDTO;
import com.booking.cinema.exception.BusinessRuleException;
import com.booking.cinema.model.Booking;
import com.booking.cinema.model.BookingState;
import com.booking.cinema.model.Payment;
import com.booking.cinema.model.PaymentStatus;
import com.booking.cinema.repository.BookingRepository;
import com.booking.cinema.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PaymentService {

    @Value("${api.stripe.key}")
    private String stripeSecretKey;
    @Value("${app.frontend.url}")
    private String frontendUrl;
    private final PaymentRepository paymentRepository;
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    public PaymentService(PaymentRepository paymentRepository, BookingService bookingService, BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }

    @PostConstruct
    void init() {
        Stripe.apiKey = this.stripeSecretKey;
    }

    @Transactional
    public PaymentResponseDTO createPayment(BookingRequestId bg) {
        Booking bookingResponse = bookingRepository.findById(bg.bookingId()).orElseThrow(() -> new BusinessRuleException("Reserva não encontrada"));

        if (bookingResponse.getState() == BookingState.CANCELED || bookingResponse.getState() == BookingState.APPROVED || bookingResponse.getState() == BookingState.EXPIRED) {
            throw new BusinessRuleException("Reserva ja encerrada ou aprovada");
        }

        Optional<Payment> existingPayment = paymentRepository.findByBookingId(bookingResponse.getId());
        if (existingPayment.isPresent() && existingPayment.get().getStatus() == PaymentStatus.PENDING) {
            throw new BusinessRuleException("Checkout já iniciado");
        }

        Long cents = bookingResponse.getPrice().multiply(new BigDecimal(100)).longValue();
        Payment payment = existingPayment.orElse(new Payment());

        SessionCreateParams params = SessionCreateParams
                .builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendUrl + "/success")
                .setCancelUrl(frontendUrl + "/canceled")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("brl")
                                                .setUnitAmount(cents)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Ingresso de Cinema - Reserva #" + bookingResponse.getId())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        try {
            Session session = Session.create(params);

            payment.setBooking(bookingResponse);
            payment.setExternalTransactionId(session.getId());
            payment.setStatus(PaymentStatus.PENDING);
            payment.setAmount(bookingResponse.getPrice());
            payment.setPaymentMethod("");

            Payment saved = paymentRepository.save(payment);

            return new PaymentResponseDTO(
                    saved.getExternalTransactionId(),
                    saved.getAmount(),
                    session.getUrl()
            );

        } catch (StripeException e) {
            throw new BusinessRuleException("Erro ao comunicar com o provedor de pagamento: " + e.getMessage());
        }
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
