package com.curso.estoque2026.api;

import com.curso.estoque2026.api.dto.CategoriaBebidaAtualizacaoRequest;
import com.curso.estoque2026.api.dto.CategoriaBebidaRequest;
import com.curso.estoque2026.api.dto.CategoriaBebidaResponse;
import com.curso.estoque2026.api.dto.PaginaResponse;
import com.curso.estoque2026.api.dto.StatusRequest;
import com.curso.estoque2026.api.mapper.CategoriaBebidaMapper;
import com.curso.estoque2026.application.CategoriaBebidaService;
import com.curso.estoque2026.domain.CategoriaBebida;
import com.curso.estoque2026.domain.Status;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

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
    public ResponseEntity<PaginaResponse<CategoriaBebidaResponse>> pesquisar(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) Status status,
        @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(PaginaResponse.de(
            service.pesquisar(nome, status, pageable),
            mapper::toResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaBebidaResponse> alterar(
        @PathVariable Long id,
        @Valid @RequestBody CategoriaBebidaAtualizacaoRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.alterar(id, request.nome())));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CategoriaBebidaResponse> alterarStatus(
        @PathVariable Long id,
        @Valid @RequestBody StatusRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.alterarStatus(id, request.status())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
