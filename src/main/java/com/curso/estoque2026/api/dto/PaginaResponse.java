package com.curso.estoque2026.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;

// Contrato estável de paginação, sem expor a estrutura interna do Spring Data.
@Schema(description = "Página de resultados de uma pesquisa")
public record PaginaResponse<T>(
    @Schema(description = "Itens da página") List<T> conteudo,
    @Schema(description = "Número da página, começando em 0", example = "0") int pagina,
    @Schema(description = "Tamanho da página", example = "20") int tamanho,
    @Schema(description = "Total de registros encontrados", example = "42") long totalElementos,
    @Schema(description = "Total de páginas", example = "3") int totalPaginas,
    @Schema(description = "Indica se é a primeira página", example = "true") boolean primeira,
    @Schema(description = "Indica se é a última página", example = "false") boolean ultima) {

    public static <S, T> PaginaResponse<T> de(Page<S> pagina, Function<S, T> conversor) {
        return new PaginaResponse<>(
            pagina.getContent().stream().map(conversor).toList(),
            pagina.getNumber(),
            pagina.getSize(),
            pagina.getTotalElements(),
            pagina.getTotalPages(),
            pagina.isFirst(),
            pagina.isLast());
    }
}
