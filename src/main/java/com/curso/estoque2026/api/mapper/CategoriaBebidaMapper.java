package com.curso.estoque2026.api.mapper;
import com.curso.estoque2026.api.dto.CategoriaBebidaRequest;
import com.curso.estoque2026.api.dto.CategoriaBebidaResponse;
import com.curso.estoque2026.domain.CategoriaBebida;
import org.springframework.stereotype.Component;
 @Component public class CategoriaBebidaMapper { public CategoriaBebida toEntity(CategoriaBebidaRequest request) { return new CategoriaBebida(request.nome()); } public CategoriaBebidaResponse toResponse(CategoriaBebida categoria) { return new CategoriaBebidaResponse( categoria.getId(), categoria.getNome(), categoria.getStatus() ); } }

