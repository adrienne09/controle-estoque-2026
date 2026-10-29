package com.curso.estoque2026.api.mapper;

import com.curso.estoque2026.api.dto.ClienteResponse;
import com.curso.estoque2026.domain.Cliente;
import com.curso.estoque2026.domain.Pessoa;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {
    public ClienteResponse toResponse(Cliente cliente) {
        Pessoa pessoa = cliente.getPessoa();
        return new ClienteResponse(cliente.getId(), pessoa.getId(), pessoa.getNome(), pessoa.getEmail(),
                pessoa.getCpf(), cliente.getTelefone(), pessoa.getStatus());
    }
}
