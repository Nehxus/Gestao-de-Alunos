package br.com.gestao.alunos.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;
    
    @Mock
    private WebRequest webRequest;
    
    @BeforeEach
    void setUp() {
        when(webRequest.getDescription(false)).thenReturn("uri=/api/alunos");
    }
    
    @Test
    void handleResourceNotFoundException_DeveRetornar404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Recurso não encontrado");
        
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceNotFoundException(ex, webRequest);
        
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Recurso não encontrado", response.getBody().getError());
        assertEquals("Recurso não encontrado", response.getBody().getMessage());
    }
    
    @Test
    void handleBusinessException_DeveRetornar400() {
        BusinessException ex = new BusinessException("Erro de negócio");
        
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessException(ex, webRequest);
        
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Erro de negócio", response.getBody().getError());
        assertEquals("Erro de negócio", response.getBody().getMessage());
    }
    
    @Test
    void handleMethodArgumentNotValidException_DeveRetornar400ComDetalhes() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        FieldError fieldError1 = new FieldError("alunoDTO", "nome", "Nome é obrigatório");
        FieldError fieldError2 = new FieldError("alunoDTO", "email", "Email inválido");
        List<FieldError> fieldErrors = List.of(fieldError1, fieldError2);
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(ex, webRequest);
        
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Erro de validação", response.getBody().getError());
        assertNotNull(response.getBody().getDetails());
        assertEquals(2, response.getBody().getDetails().size());
    }
    
    @Test
    void handleConstraintViolationException_DeveRetornar400ComDetalhes() {
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        
        ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
        ConstraintViolation<?> violation2 = mock(ConstraintViolation.class);
        
        when(violation1.getMessage()).thenReturn("Valor inválido 1");
        when(violation2.getMessage()).thenReturn("Valor inválido 2");
        
        violations.add(violation1);
        violations.add(violation2);
        
        when(ex.getConstraintViolations()).thenReturn(violations);
        
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleConstraintViolationException(ex, webRequest);
        
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Erro de validação", response.getBody().getError());
        assertNotNull(response.getBody().getDetails());
        assertEquals(2, response.getBody().getDetails().size());
    }
    
    @Test
    void handleGlobalException_DeveRetornar500() {
        Exception ex = new Exception("Erro interno");
        
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGlobalException(ex, webRequest);
        
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Erro interno do servidor", response.getBody().getError());
        assertEquals("Erro interno", response.getBody().getMessage());
    }
}

