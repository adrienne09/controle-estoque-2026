package com.curso.estoque2026.api;

import com.curso.estoque2026.api.dto.CategoriaBebidaRequest;
import com.curso.estoque2026.api.dto.CategoriaBebidaResponse;
import com.curso.estoque2026.api.mapper.CategoriaBebidaMapper;
import com.curso.estoque2026.application.CategoriaBebidaService;
import com.curso.estoque2026.domain.CategoriaBebida;
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
@RequestMapping("/api/categorias-bebida")
public class CategoriaBebidaController {

    private final CategoriaBebidaService service;
    private final CategoriaBebidaMapper mapper;

    public CategoriaBebidaController(
        CategoriaBebidaService service,
        CategoriaBebidaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CategoriaBebidaResponse> cadastrar(
        @Valid @RequestBody CategoriaBebidaRequest request,
        UriComponentsBuilder uriBuilder) {

        CategoriaBebida categoria = service.cadastrar(request.nome());
        CategoriaBebidaResponse response = mapper.toResponse(categoria);

        URI location = uriBuilder
            .path("/api/categorias-bebida/{id}")
            .buildAndExpand(categoria.getId())
            .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaBebidaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.buscarPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaBebidaResponse>> listar() {
        List<CategoriaBebidaResponse> corpo = service.listar()
            .stream()
            .map(mapper::toResponse)
            .toList();
        return ResponseEntity.ok(corpo);
    }
}
