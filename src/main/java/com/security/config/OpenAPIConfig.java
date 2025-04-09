package com.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Suporte",
                        email = "suporte@exemplo.com",
                        url = "https://exemplo.com"
                ),
                description = "JWT Security Api Documentation",
                title = "JWT Security Api",
                version = "1.0",
                license = @License(
                        name = "Licença da API",
                        url = "https://exemplo.com/licenca"
                ),
                termsOfService = "https://exemplo.com/termos"
        ),
        servers = {
                @Server(
                        description = "Ambiente Local",
                        url = "http://localhost:8084"
                ),
                @Server(
                        description = "Ambiente de Produção",
                        url = "https://api.exemplo.com"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfig {
}