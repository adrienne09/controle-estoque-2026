package com.curso.estoque2026.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FornecedorTest {

    @Test
    void deveCriarFornecedorAtivo() {
        Fornecedor fornecedor = new Fornecedor(
                "Distribuidora de Bebidas Brasil LTDA",
                "11222333000181");

        assertEquals("Distribuidora de Bebidas Brasil LTDA", fornecedor.getRazaoSocial());
        assertEquals("11222333000181", fornecedor.getCnpj());
        assertEquals(Status.ATIVO, fornecedor.getStatus());
    }

    @Test
    void deveRejeitarCnpjComFormatoInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Fornecedor("Distribuidora", "12.345"));
    }

    @Test
    void deveRejeitarCnpjComLetras() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Fornecedor("Distribuidora", "1234567800019A"));
    }

    @Test
    void deveRejeitarRazaoSocialVazia() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Fornecedor(" ", "11222333000181"));
    }

    @Test
    void deveInativarEAtivarFornecedor() {
        Fornecedor fornecedor = new Fornecedor("Distribuidora", "11222333000181");

        fornecedor.inativar();
        assertEquals(Status.INATIVO, fornecedor.getStatus());

        fornecedor.ativar();
        assertEquals(Status.ATIVO, fornecedor.getStatus());
    }
}
