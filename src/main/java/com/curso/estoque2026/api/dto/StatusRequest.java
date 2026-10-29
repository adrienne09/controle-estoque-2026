package com.curso.estoque2026.api.dto;

import com.curso.estoque2026.domain.Status;
import jakarta.validation.constraints.NotNull;

public record StatusRequest(
    @NotNull(message = "Status é obrigatório")
    Status status) {
}
