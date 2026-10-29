package com.curso.estoque2026.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para alterar o nome da categoria")
public record CategoriaBebidaAtualizacaoRequest(
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve possuir no máximo 100 caracteres")
    @Schema(description = "Novo nome da categoria", example = "Sucos") String nome) {
}
