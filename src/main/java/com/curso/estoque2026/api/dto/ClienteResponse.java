package com.curso.estoque2026.api.dto;

import com.curso.estoque2026.domain.Status;

public record ClienteResponse(Long id, Long pessoaId, String nome, String email,
                              String cpf, String telefone, Status status) { }
