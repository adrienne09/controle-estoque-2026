package com.curso.estoque2026.api.dto;

import java.math.BigDecimal;

public record ItemVendaResponse(Long id, Long bebidaId, String bebidaDescricao,
                                BigDecimal quantidade, BigDecimal valorUnitario,
                                BigDecimal subtotal) { }
