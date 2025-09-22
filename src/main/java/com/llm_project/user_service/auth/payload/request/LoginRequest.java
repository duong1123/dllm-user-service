package com.llm_project.user_service.auth.payload.request;

import lombok.Data;

@Data
public final class LoginRequest {
  private String username;
  private String password;
}
