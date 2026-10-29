package com.curso.estoque2026.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

// Código de barras, saldo e status possuem fluxos próprios e não entram no PUT.
@Schema(description = "Dados comerciais alteráveis da bebida")
public record BebidaAtualizacaoRequest(
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 150, message = "Descrição deve possuir no máximo 150 caracteres")
    @Schema(description = "Descrição comercial", example = "Refrigerante Cola 2L Zero") String descricao,
    @NotNull(message = "Valor unitário é obrigatório")
    @PositiveOrZero(message = "Valor unitário não pode ser negativo")
    @Schema(description = "Preço por unidade", example = "8.90") BigDecimal valorUnitario,
    @NotNull(message = "Estoque mínimo é obrigatório")
    @PositiveOrZero(message = "Estoque mínimo não pode ser negativo")
    @Schema(description = "Limite que indica reposição", example = "10.000") BigDecimal estoqueMinimo,
    @NotNull(message = "Categoria é obrigatória")
    @Positive(message = "Identificador da categoria deve ser positivo")
    @Schema(description = "Identificador da categoria", example = "1") Long categoriaId,
    @Positive(message = "Identificador do fornecedor deve ser positivo")
    @Schema(description = "Identificador do fornecedor (opcional)", example = "1") Long fornecedorId) {
}
