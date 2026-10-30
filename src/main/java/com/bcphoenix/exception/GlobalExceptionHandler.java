package com.bcphoenix.exception;

import com.bcphoenix.dto.response.ApiError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Tes 404 et 409 levées dans les services
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatus(ResponseStatusException ex) {
        int status = ex.getStatusCode().value();
        return ResponseEntity
                .status(status)
                .body(new ApiError(status, ex.getReason(), null));
    }

    // Les erreurs de @Valid (@NotBlank, @Size, @NotNull…)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ApiError(400, "Données invalides", errors);
    }

    // Une contrainte SQL violée (unique, clé étrangère…) qui aurait échappé aux vérifications
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrity(DataIntegrityViolationException ex) {
        return new ApiError(409, "Opération impossible : conflit avec des données existantes", null);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotReadable(HttpMessageNotReadableException ex) {
        return new ApiError(400, "Requête invalide : format JSON ou valeur incorrecte", null);
    }
}