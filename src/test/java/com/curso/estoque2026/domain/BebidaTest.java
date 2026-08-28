package com.curso.estoque2026.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BebidaTest {

    @Test
    void deveCriarBebidaAtivaComDadosValidos() {
        Bebida bebida = novaBebida("3.000", "12.90");

        assertEquals("7890000000001", bebida.getCodigoBarras());
        assertEquals("Cerveja IPA 500ml", bebida.getDescricao());
        assertEquals(Status.ATIVO, bebida.getStatus());
        assertEquals(LocalDate.of(2026, 8, 27), bebida.getDataCadastro());
    }

    @Test
    void deveCalcularValorDoEstoque() {
        Bebida bebida = novaBebida("3.000", "12.90");

        BigDecimal valorEstoque = bebida.calcularValorEstoque();

        assertEquals(0, new BigDecimal("38.70").compareTo(valorEstoque));
    }

    @Test
    void deveReceberERetirarEstoque() {
        Bebida bebida = novaBebida("3.000", "12.90");

        bebida.receberEstoque(new BigDecimal("2.500"));
        bebida.retirarEstoque(new BigDecimal("1.000"));

        assertEquals(0, new BigDecimal("4.500").compareTo(bebida.getSaldoEstoque()));
    }

    @Test
    void naoDeveRetirarQuantidadeMaiorQueOSaldo() {
        Bebida bebida = novaBebida("3.000", "12.90");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> bebida.retirarEstoque(new BigDecimal("3.001")));

        assertEquals("Saldo de estoque insuficiente", excecao.getMessage());
    }

    @Test
    void naoDeveCriarBebidaComCodigoEmBranco() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Bebida(
                        "  ",
                        "Cerveja IPA 500ml",
                        BigDecimal.ZERO,
                        BigDecimal.TEN,
                        LocalDate.now()));
    }

    private Bebida novaBebida(String saldo, String valorUnitario) {
        return new Bebida(
                "7890000000001",
                "Cerveja IPA 500ml",
                new BigDecimal(saldo),
                new BigDecimal(valorUnitario),
                LocalDate.of(2026, 8, 27));
    }
}
