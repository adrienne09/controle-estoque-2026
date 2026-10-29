package com.curso.estoque2026.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

// Código de barras, saldo e status possuem fluxos próprios e não entram no PUT.
public record BebidaAtualizacaoRequest(
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 150, message = "Descrição deve possuir no máximo 150 caracteres")
    String descricao,
    @NotNull(message = "Valor unitário é obrigatório")
    @PositiveOrZero(message = "Valor unitário não pode ser negativo")
    BigDecimal valorUnitario,
    @NotNull(message = "Estoque mínimo é obrigatório")
    @PositiveOrZero(message = "Estoque mínimo não pode ser negativo")
    BigDecimal estoqueMinimo,
    @NotNull(message = "Categoria é obrigatória")
    @Positive(message = "Identificador da categoria deve ser positivo")
    Long categoriaId,
    @Positive(message = "Identificador do fornecedor deve ser positivo")
    Long fornecedorId) {
}
