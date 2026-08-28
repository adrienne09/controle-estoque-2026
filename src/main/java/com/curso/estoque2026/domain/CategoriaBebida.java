package com.curso.estoque2026.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoriaBebida {

    private final String nome;
    private Status status;
    private final List<Bebida> bebidas = new ArrayList<>();

    public CategoriaBebida(String nome) {
        this.nome = validarTextoObrigatorio(nome, "Nome da categoria é obrigatório");
        this.status = Status.ATIVO;
    }

    public void adicionarBebida(Bebida bebida) {
        Objects.requireNonNull(bebida, "Bebida é obrigatória");

        boolean codigoJaUtilizado = bebidas.stream()
                .anyMatch(item -> item != bebida
                        && item.getCodigoBarras().equals(bebida.getCodigoBarras()));

        if (codigoJaUtilizado) {
            throw new IllegalArgumentException("Código de barras já utilizado na categoria");
        }

        bebida.associarA(this);

        if (!bebidas.contains(bebida)) {
            bebidas.add(bebida);
        }
    }

    public void ativar() {
        this.status = Status.ATIVO;
    }

    public void inativar() {
        this.status = Status.INATIVO;
    }

    public String getNome() {
        return nome;
    }

    public Status getStatus() {
        return status;
    }

    public List<Bebida> getBebidas() {
        return List.copyOf(bebidas);
    }

    private static String validarTextoObrigatorio(String texto, String mensagem) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return texto.trim();
    }
}
