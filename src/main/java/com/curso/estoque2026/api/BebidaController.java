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
@Tag(name = "Bebidas", description = "Cadastro, pesquisa e movimentação de estoque das bebidas")
@RequestMapping("/api/bebidas")
public class BebidaController {

    private final BebidaService service;
    private final BebidaMapper mapper;

    public BebidaController(BebidaService service, BebidaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Cadastrar bebida")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Bebida cadastrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "404", description = "Categoria ou fornecedor não encontrado",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "409", description = "Código de barras já cadastrado",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
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

    @Operation(summary = "Consultar bebida por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bebida encontrada"),
        @ApiResponse(responseCode = "404", description = "Bebida não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BebidaResponse> buscarPorId(@Parameter(description = "Identificador da bebida", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.buscarPorId(id)));
    }

    @Operation(summary = "Pesquisar bebidas",
        description = "Filtra por descrição, status, categoria, fornecedor e bebidas abaixo do estoque mínimo, com paginação.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Página de bebidas"),
        @ApiResponse(responseCode = "400", description = "Ordenação ou filtro inválido",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
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

    @Operation(summary = "Alterar dados comerciais da bebida",
        description = "Código de barras, saldo e status têm fluxos próprios e não mudam aqui.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bebida alterada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "404", description = "Bebida, categoria ou fornecedor não encontrado",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<BebidaResponse> alterar(
        @Parameter(description = "Identificador da bebida", example = "1") @PathVariable Long id,
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

    @Operation(summary = "Ativar ou inativar bebida")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status alterado"),
        @ApiResponse(responseCode = "400", description = "Status inválido",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "404", description = "Bebida não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<BebidaResponse> alterarStatus(
        @Parameter(description = "Identificador da bebida", example = "1") @PathVariable Long id,
        @Valid @RequestBody StatusRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.alterarStatus(id, request.status())));
    }

    @Operation(summary = "Registrar entrada de estoque")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Saldo atualizado"),
        @ApiResponse(responseCode = "400", description = "Quantidade inválida",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "404", description = "Bebida não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @PostMapping("/{id}/estoque/entradas")
    public ResponseEntity<BebidaResponse> receberEstoque(
        @Parameter(description = "Identificador da bebida", example = "1") @PathVariable Long id,
        @Valid @RequestBody MovimentacaoEstoqueRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.receberEstoque(id, request.quantidade())));
    }

    @Operation(summary = "Registrar saída de estoque")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Saldo atualizado"),
        @ApiResponse(responseCode = "400", description = "Quantidade inválida ou saldo insuficiente",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "404", description = "Bebida não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @PostMapping("/{id}/estoque/saidas")
    public ResponseEntity<BebidaResponse> retirarEstoque(
        @Parameter(description = "Identificador da bebida", example = "1") @PathVariable Long id,
        @Valid @RequestBody MovimentacaoEstoqueRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.retirarEstoque(id, request.quantidade())));
    }

    @Operation(summary = "Excluir bebida")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Bebida excluída"),
        @ApiResponse(responseCode = "404", description = "Bebida não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@Parameter(description = "Identificador da bebida", example = "1") @PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
