package com.curso.estoque2026.api;

import com.curso.estoque2026.api.dto.FornecedorRequest;
import com.curso.estoque2026.api.dto.FornecedorResponse;
import com.curso.estoque2026.api.mapper.FornecedorMapper;
import com.curso.estoque2026.application.FornecedorService;
import com.curso.estoque2026.domain.Fornecedor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.curso.estoque2026.api.dto.ErroResponse;
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
@Tag(name = "Fornecedores", description = "Cadastro e consulta de fornecedores")
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    private final FornecedorService service;
    private final FornecedorMapper mapper;

    public FornecedorController(FornecedorService service, FornecedorMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Cadastrar fornecedor")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Fornecedor cadastrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
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

    @Operation(summary = "Consultar fornecedor por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
        @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponse> buscarPorId(@Parameter(description = "Identificador do fornecedor", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.buscarPorId(id)));
    }

    @Operation(summary = "Listar fornecedores")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fornecedores listados")
    })
    @GetMapping
    public ResponseEntity<List<FornecedorResponse>> listar() {
        List<FornecedorResponse> corpo = service.listar()
            .stream()
            .map(mapper::toResponse)
            .toList();
        return ResponseEntity.ok(corpo);
    }
}
