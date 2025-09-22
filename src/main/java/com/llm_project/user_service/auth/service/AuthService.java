package com.llm_project.user_service.auth.service;

import com.llm_project.user_service.auth.payload.request.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
  public ResponseEntity<Object> login(LoginRequest loginRequest);


}
