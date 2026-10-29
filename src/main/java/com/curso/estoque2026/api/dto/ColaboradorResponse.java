package com.curso.estoque2026.api.dto;

import com.curso.estoque2026.domain.FuncaoColaborador;
import com.curso.estoque2026.domain.Status;

import java.time.LocalDate;

public record ColaboradorResponse(Long id, Long pessoaId, String nome, String email,
                                  String cpf, Status status, String matricula,
                                  FuncaoColaborador funcao, LocalDate dataAdmissao) { }
