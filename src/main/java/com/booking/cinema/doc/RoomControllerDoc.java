package com.booking.cinema.doc;

import com.booking.cinema.dto.request.RoomRequestDTO;
import com.booking.cinema.dto.response.ErrorResponseDTO;
import com.booking.cinema.dto.response.RoomResponseDTO;
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
        name = "Rooms",
        description = "Criar, buscar, atualizar e deletar salas de cinema"
)
public interface RoomControllerDoc {

    @Operation(
            summary = "Criar Sala",
            description = "Cria uma nova sala no sistema",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Sala criada com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação ao criar a sala",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<RoomResponseDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para criar uma sala",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RoomRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Sala válida",
                                    value = """
                                            {
                                            "name": "Sala 1 - IMAX",
                                            "totalRows": 150,
                                            "seatPerRows": 10
                                            }
                                            """
                            )))
            @Valid
            @RequestBody
            RoomRequestDTO request);

    @Operation(
            summary = "Busca uma sala",
            description = "Busca uma sala específica pelo seu ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sala retornada com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sala não encontrada",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<RoomResponseDTO> findById(
            @Parameter(description = "ID da sala", example = "1", required = true)
            @PathVariable
            Long id);

    @Operation(
            summary = "Atualizar Sala",
            description = "Atualiza os dados de uma sala existente",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sala atualizada com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sala não encontrada",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<RoomResponseDTO> update(
            @Parameter(description = "ID da sala", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RoomRequestDTO request);

    @Operation(
            summary = "Deleta uma sala",
            description = "Remove uma sala do sistema pelo seu ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Sala deletada com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sala não encontrada",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<Void> delete(
            @Parameter(description = "ID da sala", example = "1", required = true)
            @PathVariable
            Long id);

    @Operation(
            summary = "Busca todas as salas",
            description = "Lista todas as salas cadastradas",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Salas retornadas com sucesso"
                    )
            }
    )
    ResponseEntity<List<RoomResponseDTO>> getAll();
}