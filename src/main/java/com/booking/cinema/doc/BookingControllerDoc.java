package com.booking.cinema.doc;

import com.booking.cinema.dto.request.BookingRequestDTO;
import com.booking.cinema.dto.response.BookingResponseDTO;
import com.booking.cinema.dto.response.ErrorResponseDTO;
import com.booking.cinema.dto.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(
        name = "Bookings",
        description = "Criar, buscar e deletar uma reserva"
)
public interface BookingControllerDoc {

    @Operation(
            summary = "Create Booking",
            description = "Cria uma reserva para um assento",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Reserva criada com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro ao criar uma reserva",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<BookingResponseDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para criar uma reserva",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BookingRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Reserva válida",
                                    value = """
                                            {
                                            "sessionId": session-id-here,
                                            "userId": user-id-here,
                                            "seatLabel": "seat-label-here",
                                            "ticket": "MEIA_ENTRADA OR INTEIRA"
                                            }
                                            """
                            )))
            @Valid
            @RequestBody
            BookingRequestDTO request);

    @Operation(
            summary = "Busca uma reserva",
            description = "Busca uma reserva especifica",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reserva retornada com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Reserva não encontrada",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<BookingResponseDTO> findById(
            @Parameter(description = "ID da reserva", example = "2", required = true)
            @PathVariable
            Long id);

    @Operation(
            summary = "Busca todas as reserva",
            description = "Lista todas as reservas encontradas",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reservas retornada com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Não existe reservas para serem buscadas",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<List<BookingResponseDTO>> getAll();
}
