package com.curso.estoque2026.api;

import com.curso.estoque2026.api.dto.ErroResponse;
import com.curso.estoque2026.application.RecursoDuplicadoException;
import com.curso.estoque2026.application.RecursoEmUsoException;
import com.curso.estoque2026.application.RecursoNaoEncontradoException;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        return resposta(HttpStatus.NOT_FOUND, "Recurso nao encontrado", ex.getMessage());
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErroResponse> tratarDuplicado(RecursoDuplicadoException ex) {
        return resposta(HttpStatus.CONFLICT, "Recurso duplicado", ex.getMessage());
    }

    @ExceptionHandler(RecursoEmUsoException.class)
    public ResponseEntity<ErroResponse> tratarEmUso(RecursoEmUsoException ex) {
        return resposta(HttpStatus.CONFLICT, "Recurso em uso", ex.getMessage());
    }

    // Última defesa: o banco recusou por unicidade ou chave estrangeira.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> tratarIntegridade(DataIntegrityViolationException ex) {
        return resposta(HttpStatus.CONFLICT, "Conflito de dados",
            "A operacao viola uma regra de integridade do banco de dados");
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErroResponse> tratarArgumentoInvalido(RuntimeException ex) {
        return resposta(HttpStatus.BAD_REQUEST, "Dados invalidos", ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarCorpoInvalido(HttpMessageNotReadableException ex) {
        return resposta(HttpStatus.BAD_REQUEST, "Dados invalidos",
            "Corpo da requisicao ausente ou em formato invalido");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tratarParametroInvalido(MethodArgumentTypeMismatchException ex) {
        return resposta(HttpStatus.BAD_REQUEST, "Dados invalidos",
            "Parametro invalido: " + ex.getName());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors()
            .stream()
            .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
            .toList();
        ErroResponse corpo = new ErroResponse(
            HttpStatus.BAD_REQUEST.value(), "Dados invalidos", "Um ou mais campos estao invalidos", detalhes);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpo);
    }

    private ResponseEntity<ErroResponse> resposta(HttpStatus status, String erro, String mensagem) {
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), erro, mensagem));
    }
}
