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
                        name = "Support",
                        email = "support@example.com",
                        url = "https://example.com"
                ),
                description = "JWT Security Api Documentation",
                title = "JWT Security Api",
                version = "1.0",
                license = @License(
                        name = "API License",
                        url = "https://example.com/licenca"
                ),
                termsOfService = "https://example.com/termos"
        ),
        servers = {
                @Server(
                        description = "Local Environment",
                        url = "http://localhost:8084"
                ),
                @Server(
                        description = "Production Environment",
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