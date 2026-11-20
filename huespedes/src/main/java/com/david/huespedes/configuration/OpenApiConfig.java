package com.david.huespedes.configuration;

//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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

        return new OpenAPI().info(new Info()
                .title("API de Huespedes")
                .version("1.0.0")
                .description("API para la gestión de los huespedes"))
                //para activar la url de gateway
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/api/huespedes")
                                .description("Servidor de API de Gateway"),
                        new Server()
                                .url("http://localhost:8081")
                                .description("Servidor de localhost")
                ))                    .components(new Components().addSecuritySchemes(schemaName,
                            new SecurityScheme()
                                    .name(schemaName)
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")

                    )).addSecurityItem(new SecurityRequirement().addList(schemaName));


    }

}
