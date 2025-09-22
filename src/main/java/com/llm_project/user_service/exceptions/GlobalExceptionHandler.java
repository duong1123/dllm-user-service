package com.llm_project.user_service.exceptions;

import com.llm_project.user_service.common.payload.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ErrorException.class)
  public ResponseEntity<ErrorResponse> handleErrorException(ErrorException ex) {
    return ResponseEntity.status(ex.getStatus())
        .body(ex.getErrorResponse());
  }
}
