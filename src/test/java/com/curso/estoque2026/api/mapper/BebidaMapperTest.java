package com.curso.estoque2026.api.mapper;

import com.curso.estoque2026.api.dto.BebidaRequest;
import com.curso.estoque2026.api.dto.BebidaResponse;
import com.curso.estoque2026.domain.Bebida;
import com.curso.estoque2026.domain.CategoriaBebida;
import com.curso.estoque2026.domain.Fornecedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class BebidaMapperTest {

    private BebidaMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BebidaMapper();
    }

    @Test
    void deveConverterRequestParaEntity() {
        BebidaRequest request = new BebidaRequest(
            "7891234567890",
            "Refrigerante Cola 2L",
            new BigDecimal("50.000"),
            new BigDecimal("8.50"),
            new BigDecimal("10.000"),
            1L,
            2L
        );

        Bebida entity = mapper.toEntity(request);

        assertEquals("7891234567890", entity.getCodigoBarras());
        assertEquals("Refrigerante Cola 2L", entity.getDescricao());
        assertEquals(new BigDecimal("50.000"), entity.getSaldoEstoque());
        assertEquals(new BigDecimal("8.50"), entity.getValorUnitario());
        assertEquals(new BigDecimal("10.000"), entity.getEstoqueMinimo());
        assertNotNull(entity.getDataCadastro());
    }

    @Test
    void deveConverterEntityParaResponseComCategoriaEFornecedor() {
        CategoriaBebida categoria = new CategoriaBebida("Refrigerantes");
        Fornecedor fornecedor = new Fornecedor("Ambev", "02808708000107");

        Bebida bebida = new Bebida(
            "7891234567890",
            "Guaraná Antarctica 2L",
            new BigDecimal("100.000"),
            new BigDecimal("7.99"),
            new BigDecimal("20.000"),
            LocalDate.of(2026, 9, 17)
        );
        categoria.adicionarBebida(bebida);
        bebida.associarFornecedor(fornecedor);

        BebidaResponse response = mapper.toResponse(bebida);

        assertEquals("7891234567890", response.codigoBarras());
        assertEquals("Guaraná Antarctica 2L", response.descricao());
        assertEquals("Refrigerantes", response.categoriaNome());
        assertEquals("Ambev", response.fornecedorRazaoSocial());
        assertEquals(new BigDecimal("799.00"), response.valorEstoque());
    }

    @Test
    void deveConverterEntityParaResponseSemFornecedor() {
        CategoriaBebida categoria = new CategoriaBebida("Águas");

        Bebida bebida = new Bebida(
            "7899876543210",
            "Água Mineral 500ml",
            new BigDecimal("200.000"),
            new BigDecimal("2.50"),
            new BigDecimal("30.000"),
            LocalDate.of(2026, 9, 17)
        );
        categoria.adicionarBebida(bebida);

        BebidaResponse response = mapper.toResponse(bebida);

        assertEquals("7899876543210", response.codigoBarras());
        assertEquals("Águas", response.categoriaNome());
        assertNull(response.fornecedorId());
        assertNull(response.fornecedorRazaoSocial());
    }
}
