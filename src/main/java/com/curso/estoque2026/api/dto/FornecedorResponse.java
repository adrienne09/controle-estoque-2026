package com.curso.estoque2026.api.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import com.curso.estoque2026.domain.Status;
@Schema(description = "Representação de um fornecedor")
public record FornecedorResponse( @Schema(description = "Identificador do fornecedor", example = "1") Long id, @Schema(description = "Razão social", example = "Distribuidora de Bebidas Central Ltda.") String razaoSocial, @Schema(description = "CNPJ apenas com números", example = "11222333000181") String cnpj, @Schema(description = "Situação do fornecedor", example = "ATIVO") Status status ) { }


