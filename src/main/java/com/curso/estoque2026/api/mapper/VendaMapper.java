package com.curso.estoque2026.api.mapper;

import com.curso.estoque2026.api.dto.ItemVendaResponse;
import com.curso.estoque2026.api.dto.VendaResponse;
import com.curso.estoque2026.api.dto.VendaResumoResponse;
import com.curso.estoque2026.domain.ItemVenda;
import com.curso.estoque2026.domain.Venda;
import org.springframework.stereotype.Component;

@Component
public class VendaMapper {
    public VendaResponse toResponse(Venda venda) {
        return new VendaResponse(venda.getId(), venda.getCliente().getId(), venda.getCliente().getPessoa().getNome(),
                venda.getVendedor().getId(), venda.getVendedor().getPessoa().getNome(), venda.getDataVenda(),
                venda.getStatus(), venda.getItens().stream().map(this::toItemResponse).toList(), venda.calcularTotal());
    }

    public VendaResumoResponse toResumo(Venda venda) {
        return new VendaResumoResponse(venda.getId(), venda.getCliente().getId(),
                venda.getCliente().getPessoa().getNome(), venda.getVendedor().getId(),
                venda.getVendedor().getPessoa().getNome(), venda.getDataVenda(), venda.getStatus());
    }

    private ItemVendaResponse toItemResponse(ItemVenda item) {
        return new ItemVendaResponse(item.getId(), item.getBebida().getId(), item.getBebida().getDescricao(),
                item.getQuantidade(), item.getValorUnitario(), item.calcularSubtotal());
    }
}
