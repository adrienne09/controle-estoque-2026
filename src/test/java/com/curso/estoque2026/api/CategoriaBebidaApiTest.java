package com.curso.estoque2026.api;

import com.curso.estoque2026.domain.Bebida;
import com.curso.estoque2026.domain.CategoriaBebida;
import com.curso.estoque2026.repository.BebidaRepository;
import com.curso.estoque2026.repository.CategoriaBebidaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CategoriaBebidaApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaBebidaRepository categoriaRepository;

    @Autowired
    private BebidaRepository bebidaRepository;

    @Test
    void devePesquisarCategoriasPorNomeComPaginacao() throws Exception {
        categoriaRepository.save(new CategoriaBebida("Licores Pesquisa"));
        categoriaRepository.save(new CategoriaBebida("Cachacas Pesquisa"));

        mockMvc.perform(get("/api/categorias-bebida")
                        .param("nome", "licores pesq")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo.length()").value(1))
                .andExpect(jsonPath("$.conteudo[0].nome").value("Licores Pesquisa"))
                .andExpect(jsonPath("$.tamanho").value(10));
    }

    @Test
    void deveAlterarNomeDaCategoria() throws Exception {
        CategoriaBebida categoria = categoriaRepository.save(new CategoriaBebida("Refris Alterar"));

        mockMvc.perform(put("/api/categorias-bebida/{id}", categoria.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Refrigerantes Alterados\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Refrigerantes Alterados"));
    }

    @Test
    void deveRetornar409AoAlterarParaNomeDeOutraCategoria() throws Exception {
        categoriaRepository.save(new CategoriaBebida("Agua Tonica Duplicada"));
        CategoriaBebida outra = categoriaRepository.save(new CategoriaBebida("Agua Saborizada"));

        mockMvc.perform(put("/api/categorias-bebida/{id}", outra.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"agua tonica duplicada\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("Nome da categoria já cadastrado"));
    }

    @Test
    void deveInativarCategoria() throws Exception {
        CategoriaBebida categoria = categoriaRepository.save(new CategoriaBebida("Sidras Status"));

        mockMvc.perform(put("/api/categorias-bebida/{id}/status", categoria.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"INATIVO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INATIVO"));
    }

    @Test
    void deveExcluirCategoriaSemBebidas() throws Exception {
        CategoriaBebida categoria = categoriaRepository.save(new CategoriaBebida("Categoria Vazia"));

        mockMvc.perform(delete("/api/categorias-bebida/{id}", categoria.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar409AoExcluirCategoriaComBebidas() throws Exception {
        CategoriaBebida categoria = categoriaRepository.save(new CategoriaBebida("Categoria Em Uso"));
        Bebida bebida = new Bebida("7891999000102", "Refrigerante Guarana 2L", new BigDecimal("5.000"),
                new BigDecimal("7.50"), LocalDate.now());
        categoria.adicionarBebida(bebida);
        bebidaRepository.save(bebida);

        mockMvc.perform(delete("/api/categorias-bebida/{id}", categoria.getId()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem")
                        .value("Categoria de bebida não pode ser excluída porque possui bebidas"));
    }
}
