package com.llm_project.user_service.auth.service.Impl;

import com.llm_project.user_service.auth.entity.Session;
import com.llm_project.user_service.auth.payload.request.LoginRequest;
import com.llm_project.user_service.auth.payload.request.OtpVerifyRequest;
import com.llm_project.user_service.auth.payload.request.RefreshTokenRequest;
import com.llm_project.user_service.auth.payload.request.SendOTPRequest;
import com.llm_project.user_service.auth.payload.response.LoginResponse;
import com.llm_project.user_service.auth.payload.response.RefreshTokenResponse;
import com.llm_project.user_service.auth.repository.SessionRepository;
import com.llm_project.user_service.auth.service.AuthService;
import com.llm_project.user_service.auth.service.OtpService;
import com.llm_project.user_service.common.constants.ErrorCode;
import com.llm_project.user_service.common.constants.enums.UserStatus;
import com.llm_project.user_service.common.payload.response.ErrorResponse;
import com.llm_project.user_service.common.security.JwtUtils;
import com.llm_project.user_service.common.security.service.UserDetailsImpl;
import com.llm_project.user_service.user.entity.User;
import com.llm_project.user_service.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {

  @Value("${jwt.secret}")
  String jwtSecret;

  final JwtUtils jwtUtils;

  final UserRepository userRepository;

  final AuthenticationManager authenticationManager;

  final OtpService otpService;

  final SessionRepository sessionRepository;

  public ResponseEntity<?> login(LoginRequest loginRequest){
    String username = loginRequest.getUsername();
    String password = loginRequest.getPassword();

    Authentication authentication;

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

    UserDetailsImpl userDetails;
    userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtAccessCookie = jwtUtils.generateJwtCookie(userDetails, false);

    ResponseCookie jwtRefreshCookie = jwtUtils.generateJwtCookie(userDetails, true);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString());

    Session session = sessionRepository.findByUserId(user.getId());

    if (session == null) {
      session = Session.builder()
          .userId(user.getId())
          .accessToken(jwtAccessCookie.getValue())
          .refreshToken(jwtRefreshCookie.getValue())
          .build();
    } else {
      session.setAccessToken(jwtAccessCookie.getValue());
      session.setRefreshToken(jwtRefreshCookie.getValue());
    }

    sessionRepository.save(session);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    return ResponseEntity.ok()
        .headers(headers)
        .body(LoginResponse.builder()
            .token(jwtAccessCookie.getValue())
            .build());
  }

  @Override
  public ResponseEntity<?> refreshToken(RefreshTokenRequest refreshTokenRequest) {
    String refreshToken = refreshTokenRequest.getRefreshToken();

    Authentication authentication;
    String username;
    String roles;

    try{
      SecretKey key = Keys.hmacShaKeyFor(
          jwtSecret.getBytes(StandardCharsets.UTF_8));

      Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(refreshToken).getPayload();
      username = String.valueOf(claims.get("username"));
      roles = (String) claims.get("roles");
      authentication = new UsernamePasswordAuthenticationToken(username, null,
          AuthorityUtils.commaSeparatedStringToAuthorityList(roles));
      SecurityContextHolder.getContext().setAuthentication(authentication);

    } catch (Exception exception){
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(
              ErrorResponse.builder()
                  .code(ErrorCode.AUTHENTICATION.INVALID_REFRESH_TOKEN)
                  .value("")
                  .build()
          );
    }

    Optional<User> user = userRepository.findByUsername(username);
    String userId = user.get().getId();
    Session session = sessionRepository.findByUserId(userId);
    if (session == null || !refreshToken.equals(session.getRefreshToken())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ErrorResponse.builder()
              .code(ErrorCode.AUTHENTICATION.INVALID_REFRESH_TOKEN)
              .value("")
              .build());
    }

    ResponseCookie jwtAccessCookie = jwtUtils.generateJwtCookie(username,
        AuthorityUtils.commaSeparatedStringToAuthorityList(roles), false);

    List<String> rolesList = AuthorityUtils.commaSeparatedStringToAuthorityList(roles).stream()
        .map(GrantedAuthority::getAuthority).toList();

    ResponseCookie jwtRefreshCookie = jwtUtils.generateJwtCookie(username,
        AuthorityUtils.commaSeparatedStringToAuthorityList(roles), true);

    refreshToken = jwtRefreshCookie.getValue();

    session = sessionRepository.findByUserId(user.get().getId());

    if (session == null) {
      session = Session.builder()
          .userId(user.get().getId())
          .accessToken(jwtAccessCookie.getValue())
          .refreshToken(refreshToken)
          .build();
    } else {
      session.setAccessToken(jwtAccessCookie.getValue());
      session.setRefreshToken(refreshToken);
    }

    sessionRepository.save(session);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString());

    return ResponseEntity.ok()
        .headers(headers)
        .body(
            RefreshTokenResponse.builder()
                .token(jwtAccessCookie.getValue())
                .roles(rolesList)
                .build()
        );
  }

  @Override
  public ResponseEntity<?> logout() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    User user = userRepository.getUserByUsername(username);
    Session session = sessionRepository.findByUserId(user.getId());

    if (session != null) {
      sessionRepository.delete(session);
    }
    return ResponseEntity.ok()
        .headers(jwtUtils.HttpHeadersClearCookie())
        .body("");
  }

  public ResponseEntity<?> sendActiveOTP(SendOTPRequest request){
    String username = request.getUsername();
    User user = userRepository.getUserByUsername(username);

    otpService.sendOtp(user);
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<?> activeAccountOTP(OtpVerifyRequest request){
    String username = request.getUsername();
    String otp = request.getOtp();

    User user = userRepository.getUserByUsername(username);
    boolean isValid = otpService.verifyOtp(user, otp);

    if (!isValid) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(
              ErrorResponse.builder()
                  .code("INVALID_OTP")
                  .build()
          );
    }

    user.setStatus(UserStatus.ACTIVE);
    userRepository.save(user);
    return ResponseEntity.ok().build();
  }
}
