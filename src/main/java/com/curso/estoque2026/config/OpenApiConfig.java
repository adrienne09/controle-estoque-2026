package com.curso.estoque2026.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI estoqueOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Estoque 2026 API")
                .description("API didática de controle de estoque de bebidas: categorias, "
                    + "fornecedores, bebidas e movimentações de estoque.")
                .version("1.0.0")
                .contact(new Contact().name("Curso de Spring Boot 2026")));
    }
}
