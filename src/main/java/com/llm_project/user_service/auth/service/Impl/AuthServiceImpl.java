package com.llm_project.user_service.auth.service.Impl;

import com.llm_project.user_service.auth.payload.request.LoginRequest;
import com.llm_project.user_service.auth.payload.response.LoginResponse;
import com.llm_project.user_service.auth.service.AuthService;
import com.llm_project.user_service.common.payload.response.ErrorResponse;
import com.llm_project.user_service.common.security.JwtUtils;
import com.llm_project.user_service.common.security.service.UserDetailsImpl;
import com.llm_project.user_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

  JwtUtils jwtUtils;

  UserRepository userRepository;

  AuthenticationManager authenticationManager;

  public ResponseEntity<Object> login(LoginRequest loginRequest){
    String username = loginRequest.getUsername();
    String password = loginRequest.getPassword();

    Authentication authentication = null;

    var user = userRepository.getUserByUsername(username);

    if (user == null)
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(
              ErrorResponse.builder()
                  .code("USER_NOT_FOUND")
                  .build()
          );

    try {
      authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password)
      );
    } catch (Exception exception){
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(
              ErrorResponse.builder()
                  .code("INVALID_CREDENTIALS")
                  .build()
          );
    }

    UserDetailsImpl userDetails = null;
    userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());

    SecurityContextHolder.getContext().setAuthentication(authentication);
    return ResponseEntity.ok()
        .headers(headers)
        .body(LoginResponse.builder()
            .token(jwtCookie.getValue())
            .build());
  }
}
