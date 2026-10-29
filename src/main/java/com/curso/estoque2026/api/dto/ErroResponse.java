package com.curso.estoque2026.api.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant; import java.util.List;
@Schema(description = "Corpo padrão das respostas de erro")
public record ErroResponse( @Schema(description = "Momento do erro", example = "2026-10-29T12:00:00Z") Instant timestamp, @Schema(description = "Código HTTP", example = "404") int status, @Schema(description = "Tipo do erro", example = "Recurso nao encontrado") String erro, @Schema(description = "Mensagem para o cliente", example = "Bebida nao encontrada") String mensagem, @Schema(description = "Erros por campo, quando houver") List<String> detalhes )
{ public ErroResponse(int status, String erro, String mensagem)
{ this(Instant.now(), status, erro, mensagem, List.of());}
public ErroResponse(int status, String erro, String mensagem, List<String> detalhes)
{ this(Instant.now(), status, erro, mensagem, detalhes); } }
