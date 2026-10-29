package com.curso.estoque2026.api.dto;

import com.curso.estoque2026.domain.FuncaoColaborador;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ColaboradorAtualizacaoRequest(
        @NotBlank @Size(max = 30) @Schema(example = "VEN-001") String matricula,
        @NotNull @Schema(example = "GERENTE") FuncaoColaborador funcao) { }
