package com.curso.estoque2026.api;

import com.curso.estoque2026.api.dto.FornecedorRequest;
import com.curso.estoque2026.api.dto.FornecedorResponse;
import com.curso.estoque2026.api.mapper.FornecedorMapper;
import com.curso.estoque2026.application.FornecedorService;
import com.curso.estoque2026.domain.Fornecedor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    private final FornecedorService service;
    private final FornecedorMapper mapper;

    public FornecedorController(FornecedorService service, FornecedorMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<FornecedorResponse> cadastrar(
        @Valid @RequestBody FornecedorRequest request,
        UriComponentsBuilder uriBuilder) {

        Fornecedor fornecedor = service.cadastrar(request.razaoSocial(), request.cnpj());
        FornecedorResponse response = mapper.toResponse(fornecedor);

        URI location = uriBuilder
            .path("/api/fornecedores/{id}")
            .buildAndExpand(fornecedor.getId())
            .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.buscarPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<FornecedorResponse>> listar() {
        List<FornecedorResponse> corpo = service.listar()
            .stream()
            .map(mapper::toResponse)
            .toList();
        return ResponseEntity.ok(corpo);
    }
}
