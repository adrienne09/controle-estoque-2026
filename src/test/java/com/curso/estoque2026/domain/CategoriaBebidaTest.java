package com.curso.estoque2026.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoriaBebidaTest {

    @Test
    void deveCriarCategoriaAtivaComNomeValido() {
        CategoriaBebida categoria = new CategoriaBebida("Cervejas");

        assertEquals("Cervejas", categoria.getNome());
        assertEquals(Status.ATIVO, categoria.getStatus());
    }

    @Test
    void deveAdicionarBebidaECadastrarAssociacaoNosDoisLados() {
        CategoriaBebida categoria = new CategoriaBebida("Vinhos");
        Bebida bebida = novaBebida("7891000000001", "Vinho Tinto Merlot");

        categoria.adicionarBebida(bebida);

        assertEquals(1, categoria.getBebidas().size());
        assertEquals(bebida, categoria.getBebidas().get(0));
        assertEquals(categoria, bebida.getCategoria());
    }

    @Test
    void naoDevePermitirCodigosDeBarraDuplicadosNaMesmaCategoria() {
        CategoriaBebida categoria = new CategoriaBebida("Destilados");
        Bebida b1 = novaBebida("7891000000002", "Whisky 12 Anos");
        Bebida b2 = novaBebida("7891000000002", "Vodka Premium");

        categoria.adicionarBebida(b1);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> categoria.adicionarBebida(b2));

        assertEquals("Código de barras já utilizado na categoria", excecao.getMessage());
    }

    private Bebida novaBebida(String codigo, String descricao) {
        return new Bebida(
                codigo,
                descricao,
                new BigDecimal("10.000"),
                new BigDecimal("50.00"),
                LocalDate.of(2026, 8, 27));
    }

    @Test
    void deveMoverBebidaParaOutraCategoriaMantendoOsDoisLados() {
        CategoriaBebida origem = new CategoriaBebida("Sucos");
        CategoriaBebida destino = new CategoriaBebida("Nectares");
        Bebida bebida = novaBebida("7891000000099", "Nectar de Uva 1L");
        origem.adicionarBebida(bebida);

        bebida.alterarCategoria(destino);

        assertEquals(0, origem.getBebidas().size());
        assertEquals(1, destino.getBebidas().size());
        assertEquals(destino, bebida.getCategoria());
    }
}
