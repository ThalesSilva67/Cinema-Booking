package com.booking.cinema.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cinema Booking API")
                        .version("v1.0.0")
                        .description("""
                                Bem-vindo à documentação oficial da **Cinema Booking API**!
                                
                                Esta API fornece todos os endpoints necessários para gerenciar o ecossistema de um cinema moderno, desde o backoffice até a compra de ingressos.
                                
                                ### Principais Funcionalidades
                                
                                - **Catálogo:** Gerenciamento de filmes, salas e programação de sessões.
                                - **Reservas:** Seleção de assentos, definição de meia/inteira e emissão de tickets.
                                - **Pagamentos:** Checkout seguro e processamento de webhooks integrados com o **Stripe**.
                                - **Segurança:** Controle de acesso rígido baseado em perfis (RBAC) para clientes e administradores.
                                
                                ---
                              
                                """)
                        .contact(new Contact()
                                .name("Thales Oliveira")
                                .url("https://github.com/ThalesSilva67")
                                .email("thalesslva67@gmail.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
