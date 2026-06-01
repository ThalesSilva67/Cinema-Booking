package com.booking.cinema.doc;

import com.booking.cinema.dto.request.LoginRequestDTO;
import com.booking.cinema.dto.request.RegisterRequestDTO;
import com.booking.cinema.dto.response.ErrorResponseDTO;
import com.booking.cinema.dto.response.LoginResponseDTO;
import com.booking.cinema.dto.response.UserResponseDTO;
import com.booking.cinema.service.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "Account",
        description = "Register, Login, Me, Logout"
)
public interface AccountControllerDoc {

    @Operation(
            summary = "Register",
            description = "Cria uma conta para um usuário comum",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário cadastrado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<UserResponseDTO> register
            (
                    @Valid
                    @RequestBody
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Dados necessários para cadastrar um usuário",
                            required = true,
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class),
                                    examples = @ExampleObject(
                                            name = "Usuário válido",
                                            value = """
                                                    {
                                                    "name": Shin Nouzen,
                                                    "email": shinNouzen86@gmail.com,
                                                    "password": your-password-here
                                                    }
                                                    """
                                    ))
                    ) RegisterRequestDTO request
            );

    @Operation(
            summary = "Login",
            description = "Autentica um usuário",
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
    ResponseEntity<LoginResponseDTO> login
            (@RequestBody
             @io.swagger.v3.oas.annotations.parameters.RequestBody(
                     description = "Dados necessários para autenticar um usuário",
                     required = true,
                     content = @Content(schema = @Schema(implementation = LoginRequestDTO.class),
                             examples = @ExampleObject(
                                     name = "Login válido",
                                     value = """
                                             {
                                             "email": shinNouzen86@gmail.com,
                                             "password": your-password-here
                                             }
                                             """
                             ))
             )
             LoginRequestDTO user);

    @Operation(
            summary = "me",
            description = "Retorna dados do usuário logado",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Dados do usuário"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Usuário não autenticado",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            }
    )
    ResponseEntity<UserResponseDTO> me(
            @AuthenticationPrincipal
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Retorna dados do usuário logado",
                    content = @Content(schema = @Schema(implementation = CustomUserDetails.class),
                            examples = @ExampleObject(
                                    name = "Dados do usuário",
                                    value = """
                                            {
                                               "id": 1,
                                               "name": Shin Nouzen,
                                               "email": shinNouzen86@gmail.com,
                                               "role": STANDARD ou ADMINISTRADOR
                                            }
                                            """
                            )
                    ))
            CustomUserDetails userLogged);
}
