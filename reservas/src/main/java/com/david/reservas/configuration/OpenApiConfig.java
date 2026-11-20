package com.david.reservas.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI openAPI(){
            final String schemaName = "bearerAuth";

            return new OpenAPI()
                    .info( new Info()
                            .title("API de reservas")
                            .version("1.0.0")
                            .description("API para la gestión de reservas"))
                    .servers(List.of(
                            new Server()
                                    .url("http://localhost:8080/api/reservas")
                                    .description("Servidor de API gateway"),
                            new Server()
                                    .url("http://localhost:8083")
                                    .description("Servidor de localhost")
                    ))
                    .components(new Components().addSecuritySchemes(schemaName,
                            new SecurityScheme()
                                    .name(schemaName)
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")

                    )).addSecurityItem(new SecurityRequirement().addList(schemaName));
        }
    }


