package com.curso.estoque2026.api.mapper;

import com.curso.estoque2026.api.dto.BebidaRequest;
import com.curso.estoque2026.api.dto.BebidaResponse;
import com.curso.estoque2026.domain.Bebida;
import com.curso.estoque2026.domain.CategoriaBebida;
import com.curso.estoque2026.domain.Fornecedor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BebidaMapper {

    public Bebida toEntity(BebidaRequest request) {
        return new Bebida(
            request.codigoBarras(),
            request.descricao(),
            request.saldoEstoque(),
            request.valorUnitario(),
            request.estoqueMinimo(),
            LocalDate.now()
        );
    }

    public BebidaResponse toResponse(Bebida bebida) {
        CategoriaBebida categoria = bebida.getCategoria();
        Fornecedor fornecedor = bebida.getFornecedor();

        return new BebidaResponse(
            bebida.getId(),
            bebida.getCodigoBarras(),
            bebida.getDescricao(),
            bebida.getSaldoEstoque(),
            bebida.getValorUnitario(),
            bebida.getEstoqueMinimo(),
            bebida.calcularValorEstoque(),
            bebida.getDataCadastro(),
            bebida.getStatus(),
            categoria != null ? categoria.getId() : null,
            categoria != null ? categoria.getNome() : null,
            fornecedor != null ? fornecedor.getId() : null,
            fornecedor != null ? fornecedor.getRazaoSocial() : null
        );
    }
}

