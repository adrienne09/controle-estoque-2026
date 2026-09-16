package com.curso.estoque2026.api.dto;
import com.curso.estoque2026.domain.Status;
import java.math.BigDecimal; import java.time.LocalDate;
public record BebidaResponse( Long id, String codigoBarras, String descricao, BigDecimal saldoEstoque, BigDecimal valorUnitario, BigDecimal estoqueMinimo, BigDecimal valorEstoque, LocalDate dataCadastro, Status status, Long categoriaId, String categoriaNome, Long fornecedorId, String fornecedorRazaoSocial ) { }

