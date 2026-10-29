package com.curso.estoque2026.api;

import com.curso.estoque2026.domain.Bebida;
import com.curso.estoque2026.domain.CategoriaBebida;
import com.curso.estoque2026.domain.Fornecedor;
import com.curso.estoque2026.repository.BebidaRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Autowired
    private BebidaRepository bebidaRepository;

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
    void devePesquisarBebidasComPaginacaoEFiltros() throws Exception {
        CategoriaBebida categoria = categoriaRepository.save(new CategoriaBebida("Energeticos API"));
        Bebida bebida = novaBebida("7891999000041", "Energetico Lata 473ml", categoria);
        bebida.retirarEstoque(new BigDecimal("8.000"));
        bebidaRepository.save(bebida);
        bebidaRepository.save(novaBebida("7891999000058", "Cha Gelado 1L", categoria));

        mockMvc.perform(get("/api/bebidas")
                        .param("descricao", "energetico")
                        .param("categoriaId", categoria.getId().toString())
                        .param("abaixoEstoqueMinimo", "true")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo").isArray())
                .andExpect(jsonPath("$.conteudo.length()").value(1))
                .andExpect(jsonPath("$.conteudo[0].codigoBarras").value("7891999000041"))
                .andExpect(jsonPath("$.tamanho").value(5))
                .andExpect(jsonPath("$.totalElementos").value(1));
    }

    @Test
    void deveRetornar400ParaOrdenacaoInvalida() throws Exception {
        mockMvc.perform(get("/api/bebidas").param("sort", "senha,asc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Campo de ordenação inválido: senha"));
    }

    @Test
    void deveAlterarBebidaTrocandoCategoria() throws Exception {
        CategoriaBebida origem = categoriaRepository.save(new CategoriaBebida("Aguas API"));
        CategoriaBebida destino = categoriaRepository.save(new CategoriaBebida("Aguas com Gas API"));
        Bebida bebida = bebidaRepository.save(novaBebida("7891999000065", "Agua 500ml", origem));

        String json = """
                {
                  "descricao": "Agua com Gas 500ml",
                  "valorUnitario": 3.20,
                  "estoqueMinimo": 6.000,
                  "categoriaId": %d
                }
                """.formatted(destino.getId());

        mockMvc.perform(put("/api/bebidas/{id}", bebida.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Agua com Gas 500ml"))
                .andExpect(jsonPath("$.categoriaNome").value("Aguas com Gas API"))
                .andExpect(jsonPath("$.codigoBarras").value("7891999000065"));
    }

    @Test
    void deveInativarBebida() throws Exception {
        CategoriaBebida categoria = categoriaRepository.save(new CategoriaBebida("Isotonicos API"));
        Bebida bebida = bebidaRepository.save(novaBebida("7891999000072", "Isotonico 500ml", categoria));

        mockMvc.perform(put("/api/bebidas/{id}/status", bebida.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"INATIVO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INATIVO"));
    }

    @Test
    void deveMovimentarEstoqueEBloquearSaidaMaiorQueSaldo() throws Exception {
        CategoriaBebida categoria = categoriaRepository.save(new CategoriaBebida("Cervejas API"));
        Bebida bebida = bebidaRepository.save(novaBebida("7891999000089", "Cerveja Lata 350ml", categoria));

        mockMvc.perform(post("/api/bebidas/{id}/estoque/entradas", bebida.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantidade\": 6}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldoEstoque").value(16));

        mockMvc.perform(post("/api/bebidas/{id}/estoque/saidas", bebida.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantidade\": 4}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldoEstoque").value(12));

        mockMvc.perform(post("/api/bebidas/{id}/estoque/saidas", bebida.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantidade\": 100}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Saldo de estoque insuficiente"));
    }

    @Test
    void deveExcluirBebidaERetornar204() throws Exception {
        CategoriaBebida categoria = categoriaRepository.save(new CategoriaBebida("Vinhos API"));
        Bebida bebida = bebidaRepository.save(novaBebida("7891999000096", "Vinho Tinto 750ml", categoria));

        mockMvc.perform(delete("/api/bebidas/{id}", bebida.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/bebidas/{id}", bebida.getId()))
                .andExpect(status().isNotFound());
    }

    private Bebida novaBebida(String codigoBarras, String descricao, CategoriaBebida categoria) {
        Bebida bebida = new Bebida(codigoBarras, descricao, new BigDecimal("10.000"),
                new BigDecimal("5.00"), new BigDecimal("5.000"), LocalDate.now());
        categoria.adicionarBebida(bebida);
        return bebida;
    }

    @Test
    void deveListarBebidasERetornar200() throws Exception {
        mockMvc.perform(get("/api/bebidas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo").isArray())
                .andExpect(jsonPath("$.pagina").value(0));
    }
}
