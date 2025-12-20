package com.example.fintar.exception;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid (MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            });
            return ResponseUtil.errorWithData(HttpStatus.BAD_REQUEST, "Invalid request content", errors);
        } else {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseUtil.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
