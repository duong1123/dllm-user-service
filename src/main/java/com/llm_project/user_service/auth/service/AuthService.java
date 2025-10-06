package com.llm_project.user_service.auth.service;

import com.llm_project.user_service.auth.payload.request.LoginRequest;
import com.llm_project.user_service.auth.payload.request.OtpVerifyRequest;
import com.llm_project.user_service.auth.payload.request.RefreshTokenRequest;
import com.llm_project.user_service.auth.payload.request.SendOTPRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

  ResponseEntity<?> login(LoginRequest loginRequest);

  ResponseEntity<?> refreshToken(RefreshTokenRequest refreshTokenRequest);

  ResponseEntity<?> logout();

  ResponseEntity<?> sendActiveOTP(SendOTPRequest request);

  ResponseEntity<?> activeAccountOTP(OtpVerifyRequest request);
}
