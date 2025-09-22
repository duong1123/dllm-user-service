package com.llm_project.user_service.exceptions;

import com.llm_project.user_service.common.payload.response.ErrorResponse;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ErrorException extends RuntimeException {

  private HttpStatus status;
  protected ErrorResponse errorResponse;

  public ErrorException(HttpStatus status, String errorCode) {
    setStatus(status);

    ErrorResponse errorResponse = ErrorResponse.builder()
        .code(errorCode)
        .build();
    setErrorResponse(errorResponse);
  }

  public ErrorException(HttpStatus status, List<Map<String, String>> errors) {
    setStatus(status);

    ErrorResponse errorResponse = ErrorResponse.builder()
        .errors(errors)
        .build();
    setErrorResponse(errorResponse);
  }
}
