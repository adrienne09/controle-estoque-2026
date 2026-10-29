package com.curso.estoque2026.api.dto;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;

// Contrato estável de paginação, sem expor a estrutura interna do Spring Data.
public record PaginaResponse<T>(
    List<T> conteudo,
    int pagina,
    int tamanho,
    long totalElementos,
    int totalPaginas,
    boolean primeira,
    boolean ultima) {

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
