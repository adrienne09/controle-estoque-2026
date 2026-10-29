package com.curso.estoque2026.api.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import com.curso.estoque2026.domain.Status;
@Schema(description = "Representação de uma categoria de bebida")
public record CategoriaBebidaResponse( @Schema(description = "Identificador da categoria", example = "1") Long id, @Schema(description = "Nome da categoria", example = "Refrigerantes") String nome, @Schema(description = "Situação da categoria", example = "ATIVO") Status status ) { }

