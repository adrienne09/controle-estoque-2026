package com.curso.estoque2026.api;

import com.curso.estoque2026.domain.CategoriaBebida;
import com.curso.estoque2026.domain.Fornecedor;
import com.curso.estoque2026.repository.CategoriaBebidaRepository;
import com.curso.estoque2026.repository.FornecedorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BebidaApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaBebidaRepository categoriaRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Test
    void deveCadastrarBebidaERetornar201() throws Exception {
        CategoriaBebida categoria = categoriaRepository.save(new CategoriaBebida("Refrigerantes API"));
        Fornecedor fornecedor = fornecedorRepository.save(new Fornecedor(
                "Distribuidora API",
                "11222333000181"));

        String json = """
                {
                  "codigoBarras": "7891999000010",
                  "descricao": "Refrigerante Cola 2L",
                  "saldoEstoque": 24.000,
                  "valorUnitario": 8.50,
                  "estoqueMinimo": 10.000,
                  "categoriaId": %d,
                  "fornecedorId": %d
                }
                """.formatted(categoria.getId(), fornecedor.getId());

        mockMvc.perform(post("/api/bebidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.codigoBarras").value("7891999000010"))
                .andExpect(jsonPath("$.descricao").value("Refrigerante Cola 2L"))
                .andExpect(jsonPath("$.categoriaNome").value("Refrigerantes API"))
                .andExpect(jsonPath("$.fornecedorRazaoSocial").value("Distribuidora API"));
    }

    @Test
    void deveRetornar400ComErrosPorCampo() throws Exception {
        mockMvc.perform(post("/api/bebidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.mensagem").value("Um ou mais campos estao invalidos"))
                .andExpect(jsonPath("$.detalhes").isArray());
    }

    @Test
    void deveRetornar404ParaBebidaInexistente() throws Exception {
        mockMvc.perform(get("/api/bebidas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value("Bebida nao encontrada"));
    }

    @Test
    void deveRetornar404QuandoCategoriaNaoExisteAoCadastrar() throws Exception {
        String json = """
                {
                  "codigoBarras": "7891999000027",
                  "descricao": "Agua Mineral 500ml",
                  "saldoEstoque": 50.000,
                  "valorUnitario": 2.50,
                  "estoqueMinimo": 15.000,
                  "categoriaId": 999999
                }
                """;

        mockMvc.perform(post("/api/bebidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value("Categoria de bebida nao encontrada"));
    }

    @Test
    void deveRetornar409ParaCodigoBarrasDuplicado() throws Exception {
        CategoriaBebida categoria = categoriaRepository.save(new CategoriaBebida("Sucos API"));

        String json = """
                {
                  "codigoBarras": "7891999000034",
                  "descricao": "Suco de Laranja 1L",
                  "saldoEstoque": 12.000,
                  "valorUnitario": 7.00,
                  "estoqueMinimo": 5.000,
                  "categoriaId": %d
                }
                """.formatted(categoria.getId());

        mockMvc.perform(post("/api/bebidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/bebidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.mensagem").value("Codigo de barras ja cadastrado"));
    }

    @Test
    void deveListarBebidasERetornar200() throws Exception {
        mockMvc.perform(get("/api/bebidas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
