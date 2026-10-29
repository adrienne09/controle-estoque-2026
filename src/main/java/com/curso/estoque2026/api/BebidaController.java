package com.curso.estoque2026.api;

import com.curso.estoque2026.api.dto.BebidaAtualizacaoRequest;
import com.curso.estoque2026.api.dto.BebidaRequest;
import com.curso.estoque2026.api.dto.BebidaResponse;
import com.curso.estoque2026.api.dto.MovimentacaoEstoqueRequest;
import com.curso.estoque2026.api.dto.PaginaResponse;
import com.curso.estoque2026.api.dto.StatusRequest;
import com.curso.estoque2026.api.mapper.BebidaMapper;
import com.curso.estoque2026.application.BebidaService;
import com.curso.estoque2026.domain.Bebida;
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
    public ResponseEntity<PaginaResponse<BebidaResponse>> pesquisar(
        @RequestParam(required = false) String descricao,
        @RequestParam(required = false) Status status,
        @RequestParam(required = false) Long categoriaId,
        @RequestParam(required = false) Long fornecedorId,
        @RequestParam(required = false) Boolean abaixoEstoqueMinimo,
        @PageableDefault(size = 20, sort = "descricao", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(PaginaResponse.de(
            service.pesquisar(descricao, status, categoriaId, fornecedorId, abaixoEstoqueMinimo, pageable),
            mapper::toResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BebidaResponse> alterar(
        @PathVariable Long id,
        @Valid @RequestBody BebidaAtualizacaoRequest request) {
        Bebida bebida = service.alterar(
            id,
            request.descricao(),
            request.valorUnitario(),
            request.estoqueMinimo(),
            request.categoriaId(),
            request.fornecedorId());
        return ResponseEntity.ok(mapper.toResponse(bebida));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BebidaResponse> alterarStatus(
        @PathVariable Long id,
        @Valid @RequestBody StatusRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.alterarStatus(id, request.status())));
    }

    @PostMapping("/{id}/estoque/entradas")
    public ResponseEntity<BebidaResponse> receberEstoque(
        @PathVariable Long id,
        @Valid @RequestBody MovimentacaoEstoqueRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.receberEstoque(id, request.quantidade())));
    }

    @PostMapping("/{id}/estoque/saidas")
    public ResponseEntity<BebidaResponse> retirarEstoque(
        @PathVariable Long id,
        @Valid @RequestBody MovimentacaoEstoqueRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.retirarEstoque(id, request.quantidade())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
