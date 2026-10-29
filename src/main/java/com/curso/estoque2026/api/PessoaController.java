package com.curso.estoque2026.api;

import com.curso.estoque2026.api.dto.PaginaResponse;
import com.curso.estoque2026.api.dto.PessoaRequest;
import com.curso.estoque2026.api.dto.PessoaResponse;
import com.curso.estoque2026.api.dto.StatusRequest;
import com.curso.estoque2026.api.dto.ErroResponse;
import com.curso.estoque2026.api.mapper.PessoaMapper;
import com.curso.estoque2026.application.PessoaService;
import com.curso.estoque2026.domain.Pessoa;
import com.curso.estoque2026.domain.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Pessoas", description = "Identidade civil, CPF e dados compartilhados por papéis comerciais")
@RestController @RequestMapping("/api/pessoas")
public class PessoaController {
    private final PessoaService service;
    private final PessoaMapper mapper;

    public PessoaController(PessoaService service, PessoaMapper mapper) { this.service = service; this.mapper = mapper; }

    @Operation(summary = "Cadastrar pessoa")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Pessoa cadastrada"),
            @ApiResponse(responseCode = "400", description = "CPF ou dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "CPF ou e-mail já cadastrado", content = @Content(schema = @Schema(implementation = ErroResponse.class)))})
    @PostMapping
    public ResponseEntity<PessoaResponse> cadastrar(@Valid @RequestBody PessoaRequest request) {
        Pessoa pessoa = service.cadastrar(mapper.toEntity(request));
        return ResponseEntity.created(URI.create("/api/pessoas/" + pessoa.getId())).body(mapper.toResponse(pessoa));
    }

    @Operation(summary = "Consultar pessoa por ID")
    @GetMapping("/{id}") public PessoaResponse buscar(@PathVariable Long id) { return mapper.toResponse(service.buscarPorId(id)); }

    @Operation(summary = "Pesquisar pessoas", description = "Filtros opcionais combinados com paginação e ordenação.")
    @GetMapping
    public PaginaResponse<PessoaResponse> pesquisar(@RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf, @RequestParam(required = false) Status status,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return PaginaResponse.de(service.pesquisar(nome, cpf, status, pageable), mapper::toResponse);
    }

    @Operation(summary = "Alterar pessoa", description = "PUT substitui os dados civis editáveis; papéis são mantidos.")
    @PutMapping("/{id}")
    public PessoaResponse alterar(@PathVariable Long id, @Valid @RequestBody PessoaRequest request) {
        return mapper.toResponse(service.alterar(id, request.nome(), request.email(), request.cpf()));
    }

    @Operation(summary = "Ativar ou inativar pessoa")
    @PutMapping("/{id}/status")
    public PessoaResponse status(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
        return mapper.toResponse(service.alterarStatus(id, request.status()));
    }
}
