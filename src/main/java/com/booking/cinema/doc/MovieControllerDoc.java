package com.booking.cinema.doc;

import com.booking.cinema.dto.request.MovieRequestDTO;
import com.booking.cinema.dto.response.ErrorResponseDTO;
import com.booking.cinema.dto.response.MovieResponseDTO;
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
        name = "Movies",
        description = "Criar, buscar, atualizar e deletar um filme"
)
public interface MovieControllerDoc {

    @Operation(
            summary = "Criar Filme",
            description = "Cria um novo filme no sistema",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Filme criado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação ao criar o filme",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<MovieResponseDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para criar um filme",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MovieRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Filme válido",
                                    value = """
                                            {
                                            "title": "O Auto da Compadecida 2",
                                            "description": "João Grilo e Chicó em novas aventuras",
                                            "duration": 120
                                            }
                                            """
                            )))
            @Valid
            @RequestBody
            MovieRequestDTO request);

    @Operation(
            summary = "Busca um filme",
            description = "Busca um filme específico pelo seu ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Filme retornado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Filme não encontrado",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<MovieResponseDTO> findById(
            @Parameter(description = "ID do filme", example = "1", required = true)
            @PathVariable
            Long id);

    @Operation(
            summary = "Atualizar Filme",
            description = "Atualiza os dados de um filme existente",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Filme atualizado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Filme não encontrado",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<MovieResponseDTO> update(
            @Parameter(description = "ID do filme", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody MovieRequestDTO request);

    @Operation(
            summary = "Deleta um filme",
            description = "Remove um filme do sistema pelo seu ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Filme deletado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Filme não encontrado",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<Void> delete(
            @Parameter(description = "ID do filme", example = "1", required = true)
            @PathVariable
            Long id);

    @Operation(
            summary = "Busca todos os filmes",
            description = "Lista todos os filmes cadastrados no sistema",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Filmes retornados com sucesso"
                    )
            }
    )
    ResponseEntity<List<MovieResponseDTO>> getAll();
}