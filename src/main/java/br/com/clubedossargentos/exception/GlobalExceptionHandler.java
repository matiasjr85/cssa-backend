package br.com.clubedossargentos.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PessoaNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> handlePessoaNotFound(PessoaNaoEncontradaException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 404);
        body.put("erro", "NOT_FOUND");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(DependenteNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleDependenteNotFound(DependenteNaoEncontradoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 404);
        body.put("erro", "NOT_FOUND");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(RegraNegocioException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 400);
        body.put("erro", "BUSINESS_RULE");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 400);
        body.put("erro", "VALIDATION_ERROR");
        body.put("mensagem",
                ex.getBindingResult().getFieldError() != null
                        ? ex.getBindingResult().getFieldError().getDefaultMessage()
                        : "Dados inválidos.");
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegrity(DataIntegrityViolationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 409);
        body.put("erro", "DATA_INTEGRITY");
        body.put("mensagem", "Violação de integridade de dados. Verifique CPF, matrícula ou relacionamento.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(NaoAutenticadoException.class)
    public ResponseEntity<Map<String, Object>> handleNaoAutenticado(NaoAutenticadoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 401);
        body.put("erro", "UNAUTHORIZED");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<Map<String, Object>> handleAcessoNegado(AcessoNegadoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 403);
        body.put("erro", "FORBIDDEN");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleNotReadable(HttpMessageNotReadableException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 400);
        body.put("erro", "BAD_REQUEST");
        body.put("mensagem", "Dados inválidos no corpo da requisição.");
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 500);
        body.put("erro", "INTERNAL_SERVER_ERROR");
        body.put("mensagem", "Erro interno do servidor.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}