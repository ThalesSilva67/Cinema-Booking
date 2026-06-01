package com.booking.cinema.doc;

import com.booking.cinema.dto.request.SessionRequestDTO;
import com.booking.cinema.dto.response.ErrorResponseDTO;
import com.booking.cinema.dto.response.SessionResponseDTO;
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
        name = "Sessions",
        description = "Criar, Buscar, atualizar e deletar sessões"
)
public interface SessionControllerDoc {

    @Operation(
            summary = "Criar Sessão",
            description = "Cria uma nova sessão de cinema",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Sessão criada com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação (ex: conflito de horário)",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<SessionResponseDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para agendar uma sessão",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SessionRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Sessão válida",
                                    value = """
                                            {
                                            "movieId": 1,
                                            "roomId": 2,
                                            "startTime": "2024-05-10T20:00:00"
                                            "price": 20.00
                                            }
                                            """
                            )))
            @Valid
            @RequestBody
            SessionRequestDTO request);

    @Operation(
            summary = "Busca uma sessão",
            description = "Busca uma sessão específica pelo seu ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sessão retornada com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sessão não encontrada",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<SessionResponseDTO> findById(
            @Parameter(description = "ID da sessão", example = "1", required = true)
            @PathVariable
            Long id);

    @Operation(
            summary = "Atualiza uma sessão",
            description = "Atualiza as informações de uma sessão existente",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sessão atualizada com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sessão não encontrada",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<SessionResponseDTO> update(
            @Parameter(description = "ID da sessão", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody SessionRequestDTO request);

    @Operation(
            summary = "Deleta uma sessão",
            description = "Remove uma sessão do sistema pelo ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Sessão deletada com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sessão não encontrada",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<Void> delete(
            @Parameter(description = "ID da sessão", example = "1", required = true)
            @PathVariable
            Long id);

    @Operation(
            summary = "Busca todas as sessões",
            description = "Lista todas as sessões agendadas",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sessões retornadas com sucesso"
                    )
            }
    )
    ResponseEntity<List<SessionResponseDTO>> getAll();
}
