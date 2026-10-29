package com.curso.estoque2026.api.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import com.curso.estoque2026.api.validation.CnpjValido;
import jakarta.validation.constraints.Size;
@Schema(description = "Dados para cadastrar um fornecedor")
public record FornecedorRequest( @NotBlank(message = "Razão social é obrigatória") @Size(max = 150, message = "Razão social deve possuir no máximo 150 caracteres") @Schema(description = "Razão social", example = "Distribuidora de Bebidas Central Ltda.") String razaoSocial, @NotBlank(message = "CNPJ é obrigatório") @CnpjValido @Schema(description = "CNPJ apenas com números", example = "11222333000181") String cnpj ) { }

