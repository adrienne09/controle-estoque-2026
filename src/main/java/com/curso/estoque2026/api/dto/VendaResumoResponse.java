package com.curso.estoque2026.api.dto;

import com.curso.estoque2026.domain.StatusVenda;

import java.time.LocalDateTime;

public record VendaResumoResponse(Long id, Long clienteId, String clienteNome,
                                  Long vendedorId, String vendedorNome,
                                  LocalDateTime dataVenda, StatusVenda status) { }
