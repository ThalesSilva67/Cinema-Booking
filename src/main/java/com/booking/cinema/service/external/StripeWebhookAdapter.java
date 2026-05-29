package com.booking.cinema.service.external;

import com.booking.cinema.dto.request.WebHookRequestDTO;
import com.booking.cinema.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class StripeWebhookAdapter {
    @Value("${app.webhooks.stripe.secret-key}")
    private String webhookSecretKey;
    private final PaymentService paymentService;

    public StripeWebhookAdapter(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void processWebhookStripeEvent(String payload, String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecretKey);
            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer().getObject().orElseThrow();

                BigDecimal amountPaid = BigDecimal.valueOf(session.getAmountTotal()).divide(new BigDecimal(100));
                String paymentModelStatus = stripeStatus(session.getPaymentStatus());

                WebHookRequestDTO dto = new WebHookRequestDTO(session.getId(), paymentModelStatus, LocalDateTime.now(), amountPaid);

                paymentService.processWebhook(dto);

            }
        } catch (SignatureVerificationException e) {
            throw new SecurityException("Assinatura do Webhook inválida!");
        }
    }

    private String stripeStatus(String stripeStatus) {
        if("paid".equalsIgnoreCase(stripeStatus)) return "APPROVED";
        if("unpaid".equalsIgnoreCase(stripeStatus)) return "FAILED";
        return "UNKNOWN";
    }
}
