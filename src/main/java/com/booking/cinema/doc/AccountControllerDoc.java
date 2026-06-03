package com.booking.cinema.doc;

import com.booking.cinema.dto.request.LoginRequestDTO;
import com.booking.cinema.dto.request.RegisterRequestDTO;
import com.booking.cinema.dto.response.ErrorResponseDTO;
import com.booking.cinema.dto.response.LoginResponseDTO;
import com.booking.cinema.dto.response.UserResponseDTO;
import com.booking.cinema.service.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "Account",
        description = "Gerenciamento de contas, autenticação e perfil do usuário"
)
public interface AccountControllerDoc {

    @Operation(
            summary = "Cadastrar Usuário",
            description = "Cria uma nova conta para um usuário comum no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário cadastrado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados de cadastro inválidos ou e-mail já existente",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<UserResponseDTO> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para o cadastro",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Cadastro válido",
                                    value = """
                                            {
                                            "name": "Shin Nouzen",
                                            "email": "shinNouzen86@gmail.com",
                                            "password": "strong-password-123"
                                            }
                                            """
                            ))
            )
            @Valid
            @RequestBody
            RegisterRequestDTO request
    );

    @Operation(
            summary = "Realizar Login",
            description = "Autentica um usuário existente e retorna as credenciais/tokens de sessão.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário autenticado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credenciais inválidas",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<LoginResponseDTO> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais de acesso do usuário",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Login válido",
                                    value = """
                                            {
                                            "email": "shinNouzen86@gmail.com",
                                            "password": "strong-password-123"
                                            }
                                            """
                            ))
            )
            @Valid
            @RequestBody
            LoginRequestDTO user
    );

    @Operation(
            summary = "Meu Perfil",
            description = "Retorna os dados cadastrais do usuário atualmente autenticado no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Dados do usuário retornados com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Usuário não autenticado no sistema",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<UserResponseDTO> me(
            @Parameter(hidden = true)
            @AuthenticationPrincipal
            CustomUserDetails userLogged
    );
}