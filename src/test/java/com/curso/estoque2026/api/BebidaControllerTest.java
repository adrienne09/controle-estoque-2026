package com.curso.estoque2026.api;

import com.curso.estoque2026.api.dto.BebidaResponse;
import com.curso.estoque2026.api.mapper.BebidaMapper;
import com.curso.estoque2026.application.BebidaService;
import com.curso.estoque2026.domain.Bebida;
import com.curso.estoque2026.domain.CategoriaBebida;
import com.curso.estoque2026.domain.Fornecedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BebidaControllerTest {

    @Mock
    private BebidaService service;

    private BebidaMapper mapper;
    private BebidaController controller;

    @BeforeEach
    void setUp() {
        mapper = new BebidaMapper();
        controller = new BebidaController(service, mapper);
    }

    @Test
    void deveListarBebidasComSucesso() {
        CategoriaBebida categoria = new CategoriaBebida("Refrigerantes");
        Fornecedor fornecedor = new Fornecedor("Ambev", "02808708000107");
        Bebida bebida = new Bebida(
            "7891000315507",
            "Guaraná Antarctica 2L",
            new BigDecimal("120.000"),
            new BigDecimal("7.99"),
            new BigDecimal("20.000"),
            LocalDate.now()
        );
        categoria.adicionarBebida(bebida);
        bebida.associarFornecedor(fornecedor);

        when(service.listar()).thenReturn(List.of(bebida));

        ResponseEntity<List<BebidaResponse>> response = controller.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Guaraná Antarctica 2L", response.getBody().get(0).descricao());
        assertEquals("Refrigerantes", response.getBody().get(0).categoriaNome());
        assertEquals("Ambev", response.getBody().get(0).fornecedorRazaoSocial());
    }

    @Test
    void deveBuscarBebidaPorIdComSucesso() {
        CategoriaBebida categoria = new CategoriaBebida("Águas");
        Bebida bebida = new Bebida(
            "7891000100103",
            "Água Mineral 500ml",
            new BigDecimal("500.000"),
            new BigDecimal("2.50"),
            new BigDecimal("50.000"),
            LocalDate.now()
        );
        categoria.adicionarBebida(bebida);

        when(service.buscarPorId(1L)).thenReturn(bebida);

        ResponseEntity<BebidaResponse> response = controller.buscarPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Água Mineral 500ml", response.getBody().descricao());
        assertEquals("Águas", response.getBody().categoriaNome());
    }
}
