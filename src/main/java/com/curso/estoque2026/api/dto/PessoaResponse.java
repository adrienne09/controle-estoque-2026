package com.curso.estoque2026.api.dto;

import com.curso.estoque2026.domain.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PessoaResponse(Long id, String nome, String email, String cpf,
                             LocalDate dataCadastro, Status status) { }
