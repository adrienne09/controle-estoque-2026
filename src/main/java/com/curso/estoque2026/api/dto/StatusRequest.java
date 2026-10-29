package com.curso.estoque2026.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.curso.estoque2026.domain.Status;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Novo status do recurso")
public record StatusRequest(
    @NotNull(message = "Status é obrigatório")
    @Schema(description = "Situação desejada", example = "INATIVO") Status status) {
}
