package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Gestionnaire global d'exceptions utilisant les fonctionnalités modernes de Java 25
 * - Pattern Matching pour instanceof
 * - Switch Expressions
 * - Records pour les réponses d'erreur
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex,
            WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Business Rule Violation",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        // Utilisation de Stream API moderne pour la transformation
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> "%s: %s".formatted(error.getField(), error.getDefaultMessage()))
                .toList(); // Java 16+ - méthode toList() directe sur Stream

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Les donn\u00e9es fournies sont invalides",
                request.getDescription(false).replace("uri=", ""),
                details
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex,
            WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Une erreur inattendue s'est produite",
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Méthode utilitaire utilisant le Pattern Matching moderne
     * Démontre l'utilisation de switch expressions avec pattern matching
     */
    private HttpStatus getHttpStatusFromException(Exception ex) {
        return switch (ex) {
            case ResourceNotFoundException _ -> HttpStatus.NOT_FOUND;
            case BusinessException _ -> HttpStatus.BAD_REQUEST;
            case MethodArgumentNotValidException _ -> HttpStatus.BAD_REQUEST;
            case IllegalArgumentException _ -> HttpStatus.BAD_REQUEST;
            case IllegalStateException _ -> HttpStatus.CONFLICT;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    /**
     * Extrait le message d'erreur en utilisant pattern matching
     */
    private String extractErrorMessage(Exception ex) {
        return switch (ex) {
            case MethodArgumentNotValidException validationEx ->
                "Erreurs de validation: " + validationEx.getErrorCount() + " champ(s) invalide(s)";
            case RuntimeException runtimeEx when runtimeEx.getMessage() != null ->
                runtimeEx.getMessage();
            default -> "Une erreur inattendue s'est produite";
        };
    }
}
