package com.curso.estoque2026.api;

import com.curso.estoque2026.api.dto.BebidaRequest;
import com.curso.estoque2026.api.dto.BebidaResponse;
import com.curso.estoque2026.api.mapper.BebidaMapper;
import com.curso.estoque2026.application.BebidaService;
import com.curso.estoque2026.domain.Bebida;
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
@RequestMapping("/api/bebidas")
public class BebidaController {

    private final BebidaService service;
    private final BebidaMapper mapper;

    public BebidaController(BebidaService service, BebidaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<BebidaResponse> cadastrar(
        @Valid @RequestBody BebidaRequest request,
        UriComponentsBuilder uriBuilder) {

        Bebida bebida = service.cadastrar(
            mapper.toEntity(request),
            request.categoriaId(),
            request.fornecedorId());

        BebidaResponse response = mapper.toResponse(bebida);

        URI location = uriBuilder
            .path("/api/bebidas/{id}")
            .buildAndExpand(bebida.getId())
            .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BebidaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.buscarPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<BebidaResponse>> listar() {
        List<BebidaResponse> corpo = service.listar()
            .stream()
            .map(mapper::toResponse)
            .toList();
        return ResponseEntity.ok(corpo);
    }
}
