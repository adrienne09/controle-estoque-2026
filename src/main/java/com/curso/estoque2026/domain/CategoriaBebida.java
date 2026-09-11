package com.curso.estoque2026.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categoria_bebida")
public class CategoriaBebida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Bebida> bebidas = new ArrayList<>();

    protected CategoriaBebida() {
    }

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

    public Long getId() {
        return id;
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
