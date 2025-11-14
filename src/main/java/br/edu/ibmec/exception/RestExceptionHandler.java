package br.edu.ibmec.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handler global para padronizar respostas de erro e tratar validações.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> a));
        String path = extractPath(request);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(),
                "Falha de validação: verifique os campos enviados",
                path,
                "VALIDATION_FAILED",
                errors);
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiError> handleServiceException(ServiceException ex, HttpServletRequest request) {
        String code = ex.getTipo() != null ? ex.getTipo().name() : null;
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), code, request, Collections.emptyMap());
    }

    @ExceptionHandler(DaoException.class)
    public ResponseEntity<ApiError> handleDaoException(DaoException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "RESOURCE_NOT_FOUND", request, Collections.emptyMap());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno inesperado. Contate o suporte se o problema persistir.",
                "UNEXPECTED_ERROR",
                request,
                Collections.singletonMap("exception", ex.getClass().getSimpleName()));
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message, String code,
                                                   HttpServletRequest request, Map<String, String> details) {
        ApiError apiError = new ApiError(status.value(), message, request != null ? request.getRequestURI() : null, code, details);
        return ResponseEntity.status(status).body(apiError);
    }

    private String extractPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getRequest().getRequestURI();
        }
        return null;
    }
}
