package com.llm_project.user_service.auth.controller;

import com.llm_project.user_service.auth.payload.request.LoginRequest;
import com.llm_project.user_service.auth.payload.request.OtpVerifyRequest;
import com.llm_project.user_service.auth.payload.request.SendOTPRequest;
import com.llm_project.user_service.auth.service.AuthService;
import com.llm_project.user_service.user.entity.User;
import com.llm_project.user_service.user.payload.requests.UserCreationRequest;
import com.llm_project.user_service.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

  UserService userService;

  AuthService authService;

  @PostMapping("/register")
  ResponseEntity<Object> createUser(@RequestBody @Valid UserCreationRequest request) {
    User createdUser = userService.createUser(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdUser);
  }

  @PostMapping("/login")
  ResponseEntity<Object> login(@RequestBody @Valid LoginRequest request) {
    return authService.login(request);
  }

  @PostMapping("/send-active-otp")
  ResponseEntity<Object> sendActiveOtp(@RequestBody @Valid SendOTPRequest request) {
    return authService.sendActiveOTP(request);
  }

  @PostMapping("/active-account-otp")
  ResponseEntity<Object> activeAccountOTP(@RequestBody @Valid OtpVerifyRequest request) {
    return authService.activeAccountOTP(request);
  }
}
