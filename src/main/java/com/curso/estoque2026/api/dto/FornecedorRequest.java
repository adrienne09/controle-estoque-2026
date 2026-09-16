package com.curso.estoque2026.api.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
public record FornecedorRequest( @NotBlank(message = "Razão social é obrigatória") @Size(max = 150, message = "Razão social deve possuir no máximo 150 caracteres") String razaoSocial, @NotBlank(message = "CNPJ é obrigatório") @Pattern(regexp = "\\d{14}", message = "CNPJ deve conter 14 dígitos numéricos") String cnpj ) { }

