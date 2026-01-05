package com.example.fintar.exception;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.util.ResponseUtil;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  //    @ExceptionHandler(MethodArgumentNotValidException.class)
  //    @Override
  //    protected ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid
  // (MethodArgumentNotValidException ex) {
  //        BindingResult bindingResult = ex.getBindingResult();
  //        if(bindingResult.hasErrors()) {
  //            Map<String, String> errors = new HashMap<>();
  //            bindingResult.getFieldErrors().forEach(fieldError -> {
  //                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
  //            });
  //            return ResponseUtil.errorWithData(HttpStatus.BAD_REQUEST, "Invalid request content",
  // errors);
  //        } else {
  //            return ResponseUtil.error(HttpStatus.BAD_REQUEST, ex.getMessage());
  //        }
  //    }
  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

    ApiResponse<Object> response =
        ApiResponse.builder()
            .success(false)
            .message(ex.getMessage())
            .data(null)
            .code(status.value())
            .timestamp(Instant.now())
            .build();

    return new ResponseEntity<>(response, HttpStatus.valueOf(status.value()));
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    BindingResult bindingResult = ex.getBindingResult();

    Map<String, String> errors = new HashMap<>();
    bindingResult
        .getFieldErrors()
        .forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

    ApiResponse<Object> response =
        ApiResponse.builder()
            .success(false)
            .message("Invalid request content")
            .data(errors)
            .code(HttpStatus.BAD_REQUEST.value())
            .timestamp(Instant.now())
            .build();

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
      ResourceNotFoundException ex) {
    return ResponseUtil.error(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(InvalidJwtException.class)
  public ResponseEntity<ApiResponse<Object>> handleInvalidJwtException(InvalidJwtException ex) {
    return ResponseUtil.error(HttpStatus.UNAUTHORIZED, ex.getMessage());
  }

  //    @ExceptionHandler(DataIntegrityViolationException.class)
  //    public ResponseEntity<ApiResponse<Object>>
  // handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
  //        return ResponseUtil.error(HttpStatus.BAD_REQUEST, ex.getMessage());
  //    }

  @ExceptionHandler(BusinessValidationException.class)
  public ResponseEntity<ApiResponse<Object>> handleBusinessValidationException(
      BusinessValidationException ex) {
    return ResponseUtil.error(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ApiResponse<Object>> handleAuthorizationDeniedException(
      AuthorizationDeniedException ex) {
    return ResponseUtil.error(HttpStatus.UNAUTHORIZED, ex.getMessage());
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<ApiResponse<Object>> handleDisabledException(DisabledException ex) {
    return ResponseUtil.error(HttpStatus.UNAUTHORIZED, ex.getMessage());
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(
      AuthenticationException ex) {
    return ResponseUtil.error(HttpStatus.UNAUTHORIZED, ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
    ex.printStackTrace();
    return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
  }
}
