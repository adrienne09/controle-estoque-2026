package com.curso.estoque2026.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(description = "Quantidade movimentada no estoque")
public record MovimentacaoEstoqueRequest(
    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser maior que zero")
    @Schema(description = "Quantidade positiva a movimentar", example = "12.000") BigDecimal quantidade) {
}
