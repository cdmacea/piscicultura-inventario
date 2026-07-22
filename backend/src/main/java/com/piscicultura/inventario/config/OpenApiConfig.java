package com.piscicultura.inventario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadatos de la documentación OpenAPI (Swagger UI).
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pisciculturaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API Piscicultura - Inventario")
                .description("Administración de inventario, producción y ventas de la finca")
                .version("1.0.0")
                .contact(new Contact().name("Finca Piscícola")));
    }
}
