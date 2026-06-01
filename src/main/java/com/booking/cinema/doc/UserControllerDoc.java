package com.booking.cinema.doc;

import com.booking.cinema.dto.request.RegisterRequestDTO;
import com.booking.cinema.dto.response.ErrorResponseDTO;
import com.booking.cinema.dto.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "Users",
        description = "Gerenciar usuários do sistema"
)
public interface UserControllerDoc {

    @Operation(
            summary = "Registrar Admin",
            description = "Registra um novo usuário com privilégios de administrador",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Administrador registrado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação ou usuário já existente",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<UserResponseDTO> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para cadastro do administrador",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Cadastro válido",
                                    value = """
                                            {
                                            "name": "Admin Name",
                                            "email": "admin@cinema.com",
                                            "password": "securepassword123"
                                            }
                                            """
                            )))
            @Valid
            @RequestBody
            RegisterRequestDTO request);
}
