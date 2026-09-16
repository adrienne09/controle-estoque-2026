package com.curso.estoque2026.api;
import com.curso.estoque2026.api.dto.ErroResponse;
import com.curso.estoque2026.application.RecursoDuplicadoException;
import com.curso.estoque2026.application.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;
@RestControllerAdvice public class GlobalExceptionHandler { @ExceptionHandler(RecursoNaoEncontradoException.class) public ResponseEntity<ErroResponse> tratarNaoEncontrado(RecursoNaoEncontradoException ex)
{ ErroResponse corpo = new ErroResponse( HttpStatus.NOT_FOUND.value(), "Recurso nao encontrado", ex.getMessage() );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo);
} @ExceptionHandler(RecursoDuplicadoException.class) public ResponseEntity<ErroResponse> tratarDuplicado(RecursoDuplicadoException ex)
{ ErroResponse corpo = new ErroResponse( HttpStatus.CONFLICT.value(), "Recurso duplicado", ex.getMessage() );
    return ResponseEntity.status(HttpStatus.CONFLICT).body(corpo); }
    @ExceptionHandler(IllegalArgumentException.class) public ResponseEntity<ErroResponse> tratarArgumentoInvalido(IllegalArgumentException ex)
    { ErroResponse corpo = new ErroResponse( HttpStatus.BAD_REQUEST.value(), "Dados invalidos", ex.getMessage() );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpo); }
    @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<ErroResponse> tratarValidacao(MethodArgumentNotValidException ex)
    { List<String> detalhes = ex.getBindingResult().getFieldErrors() .stream() .map(erro -> erro.getField() + ": " + erro.getDefaultMessage()) .toList();
        ErroResponse corpo = new ErroResponse( HttpStatus.BAD_REQUEST.value(), "Dados invalidos", "Um ou mais campos estao invalidos", detalhes );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpo); } }
