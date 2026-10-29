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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.curso.estoque2026.api.dto.ErroResponse;
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
@Tag(name = "Categorias de bebida", description = "Cadastro, pesquisa e manutenção das categorias")
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

    @Operation(summary = "Cadastrar categoria")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Categoria cadastrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "409", description = "Nome já cadastrado",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
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

    @Operation(summary = "Consultar categoria por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaBebidaResponse> buscarPorId(@Parameter(description = "Identificador da categoria", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.buscarPorId(id)));
    }

    @Operation(summary = "Pesquisar categorias",
        description = "Filtra por trecho do nome e status, com paginação e ordenação (id, nome, status).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Página de categorias"),
        @ApiResponse(responseCode = "400", description = "Ordenação ou filtro inválido",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PaginaResponse<CategoriaBebidaResponse>> pesquisar(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) Status status,
        @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(PaginaResponse.de(
            service.pesquisar(nome, status, pageable),
            mapper::toResponse));
    }

    @Operation(summary = "Alterar nome da categoria")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoria alterada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "409", description = "Nome já cadastrado",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaBebidaResponse> alterar(
        @Parameter(description = "Identificador da categoria", example = "1") @PathVariable Long id,
        @Valid @RequestBody CategoriaBebidaAtualizacaoRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.alterar(id, request.nome())));
    }

    @Operation(summary = "Ativar ou inativar categoria")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status alterado"),
        @ApiResponse(responseCode = "400", description = "Status inválido",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<CategoriaBebidaResponse> alterarStatus(
        @Parameter(description = "Identificador da categoria", example = "1") @PathVariable Long id,
        @Valid @RequestBody StatusRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.alterarStatus(id, request.status())));
    }

    @Operation(summary = "Excluir categoria")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Categoria excluída"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "409", description = "Categoria possui bebidas",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@Parameter(description = "Identificador da categoria", example = "1") @PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
