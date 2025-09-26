package com.llm_project.user_service.auth.service;

import com.llm_project.user_service.auth.payload.request.LoginRequest;
import com.llm_project.user_service.auth.payload.request.OtpVerifyRequest;
import com.llm_project.user_service.auth.payload.request.SendOTPRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

  ResponseEntity<Object> login(LoginRequest loginRequest);

  ResponseEntity<Object> sendActiveOTP(SendOTPRequest request);

  ResponseEntity<Object> activeAccountOTP(OtpVerifyRequest request);
}
