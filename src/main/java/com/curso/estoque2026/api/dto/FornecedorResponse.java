package com.curso.estoque2026.api.dto;
import com.curso.estoque2026.domain.Status;
public record FornecedorResponse( Long id, String razaoSocial, String cnpj, Status status ) { }


