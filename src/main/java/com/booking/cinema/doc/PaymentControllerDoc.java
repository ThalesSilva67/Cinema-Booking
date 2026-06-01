package com.booking.cinema.doc;

import com.booking.cinema.dto.request.BookingRequestId;
import com.booking.cinema.dto.response.ErrorResponseDTO;
import com.booking.cinema.dto.response.PaymentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(
       name = "Payments",
        description = "Gerenciar intenções de pagamentos e webhooks"
)
public interface PaymentControllerDoc {

    @Operation(
            summary = "Criar Pagamento",
            description = "Inicia a criação de um pagamento para uma reserva",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Pagamento iniciado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro ao processar criação de pagamento",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<PaymentResponseDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID da reserva para pagamento",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BookingRequestId.class),
                            examples = @ExampleObject(
                                    name = "Reserva informada",
                                    value = """
                                            {
                                            "bookingId": 1
                                            }
                                            """
                            )))
            @Valid
            @RequestBody
            BookingRequestId request);

    @Operation(
            summary = "Stripe Webhook",
            description = "Endpoint para processamento de eventos do Stripe",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Evento processado com sucesso"
                    )
            }
    )
    ResponseEntity<Void> webhook(
            @RequestBody String payload,
            @io.swagger.v3.oas.annotations.Parameter(description = "Assinatura do Stripe enviada via header", required = true)
            @RequestHeader("Stripe-Signature") String sigHeader);
}
