package com.curso.estoque2026.api.dto;
import java.time.Instant; import java.util.List;
public record ErroResponse( Instant timestamp, int status, String erro, String mensagem, List<String> detalhes )
{ public ErroResponse(int status, String erro, String mensagem)
{ this(Instant.now(), status, erro, mensagem, List.of());}
public ErroResponse(int status, String erro, String mensagem, List<String> detalhes)
{ this(Instant.now(), status, erro, mensagem, detalhes); } }
