package com.doer.mraims.loanprocess.core.exceptions;

import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonObjectResponseDTO<String>> handleCustomException(CustomException ex) {
        CommonObjectResponseDTO<String> response = new CommonObjectResponseDTO<>();
        response.setStatus(false);
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage());
        response.setData(null);
        return ResponseEntity.status(ex.getCode()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonObjectResponseDTO<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Validation error");
        CommonObjectResponseDTO<String> response = new CommonObjectResponseDTO<>();
        response.setStatus(false);
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(errorMessage);
        response.setData(null);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonObjectResponseDTO<String>> handleGenericException(Exception ex) {
        CommonObjectResponseDTO<String> response = new CommonObjectResponseDTO<>();
        response.setStatus(false);
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("An unexpected error occurred: " + ex.getMessage());
        response.setData(null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

