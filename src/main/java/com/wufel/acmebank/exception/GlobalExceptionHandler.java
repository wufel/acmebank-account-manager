package com.wufel.acmebank.exception;

import com.wufel.acmebank.model.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({AccountNotFoundException.class})
    ResponseEntity<ApiError> handlesNotFoundException(Exception e) {
        ApiError apiError = new ApiError().error(e.getClass().toString()).errorMessage(e.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(apiError);
    }
}
