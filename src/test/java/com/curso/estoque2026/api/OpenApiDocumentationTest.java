package com.curso.estoque2026.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OpenApiDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void devePublicarContratoOpenApiComRotasEssenciais() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", startsWith("3.")))
                .andExpect(jsonPath("$.info.title").value("Estoque 2026 API"))
                .andExpect(jsonPath("$.info.version").value("1.0.0"))
                .andExpect(jsonPath("$.paths['/api/categorias-bebida/{id}'].get").exists())
                .andExpect(jsonPath("$.paths['/api/categorias-bebida/{id}'].put").exists())
                .andExpect(jsonPath("$.paths['/api/bebidas/{id}'].get.summary")
                        .value("Consultar bebida por ID"))
                .andExpect(jsonPath("$.paths['/api/bebidas/{id}'].get.responses['404'].content")
                        .exists())
                .andExpect(jsonPath("$.paths['/api/bebidas/{id}'].delete").exists())
                .andExpect(jsonPath("$.paths['/api/bebidas/{id}/estoque/entradas'].post").exists())
                .andExpect(jsonPath("$.paths['/api/bebidas/{id}/estoque/saidas'].post").exists())
                .andExpect(jsonPath("$.components.schemas.BebidaResponse").exists())
                .andExpect(jsonPath("$.components.schemas.ErroResponse").exists());
    }

    @Test
    void deveDisponibilizarSwaggerUi() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }
}
