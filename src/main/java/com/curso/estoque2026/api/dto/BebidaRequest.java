package com.curso.estoque2026.api.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
@Schema(description = "Dados para cadastrar uma bebida")
public record BebidaRequest( @NotBlank(message = "Código de barras é obrigatório") @Size(max = 50, message = "Código de barras deve possuir no máximo 50 caracteres") @Schema(description = "Código de barras único", example = "7891999000010") String codigoBarras, @NotBlank(message = "Descrição é obrigatória") @Size(max = 150, message = "Descrição deve possuir no máximo 150 caracteres") @Schema(description = "Descrição comercial", example = "Refrigerante Cola 2L") String descricao, @NotNull(message = "Saldo de estoque é obrigatório") @PositiveOrZero(message = "Saldo de estoque não pode ser negativo") @Schema(description = "Saldo inicial", example = "24.000") BigDecimal saldoEstoque, @NotNull(message = "Valor unitário é obrigatório") @PositiveOrZero(message = "Valor unitário não pode ser negativo") @Schema(description = "Preço por unidade", example = "8.50") BigDecimal valorUnitario, @NotNull(message = "Estoque mínimo é obrigatório") @PositiveOrZero(message = "Estoque mínimo não pode ser negativo") @Schema(description = "Limite que indica reposição", example = "10.000") BigDecimal estoqueMinimo, @NotNull(message = "Categoria é obrigatória") @Positive(message = "Identificador da categoria deve ser positivo") @Schema(description = "Identificador da categoria", example = "1") Long categoriaId, @Positive(message = "Identificador do fornecedor deve ser positivo") @Schema(description = "Identificador do fornecedor (opcional)", example = "1") Long fornecedorId ) { }

