package com.llm_project.user_service.common.security;

import com.llm_project.user_service.common.security.service.UserDetailsImpl;
import com.llm_project.user_service.user.repository.UserRoleRepository;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtils {

  @Value("${jwt.secret}")
  protected String jwtSecret;

  @Value("${jwt.expiration.ms}")
  private int jwtExpirationMs;

  @Value("${jwt.refresh.token.expiration.ms}")
  private int jwtRefreshTokenExpirationMs;


  public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal, boolean isRefreshToken){
    String jwt = generateTokenFromUsernameAndRole(userPrincipal.getUsername(),
        userPrincipal.getAuthorities(),
        isRefreshToken);
    return responseCookieFrom(jwt);
  }

  public ResponseCookie generateJwtCookie(String username, Collection<? extends GrantedAuthority> roles, boolean isRefreshToken){
    String jwt = generateTokenFromUsernameAndRole(username,
        roles,
        isRefreshToken);
    return responseCookieFrom(jwt);
  }

  public String generateTokenFromUsernameAndRole(String username,
                                                  Collection<? extends GrantedAuthority> authorities,
                                                  boolean isRefreshToken) {

    int tokenExpirationMs = jwtExpirationMs;

    if(isRefreshToken)
      tokenExpirationMs = jwtRefreshTokenExpirationMs;

    JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
        .subject(username)
        .claim("username", username)
        .claim("roles", buildRoles(authorities))
        .issueTime(new Date())
        .expirationTime(new Date((new Date()).getTime() + tokenExpirationMs))
        .build();

    Payload payload = new Payload(jwtClaimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(jwsHeader, payload);
    try {
      jwsObject.sign(new com.nimbusds.jose.crypto.MACSigner(jwtSecret));
      return jwsObject.serialize();
    } catch (Exception e) {
      log.error("Error signing the JWT: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private String buildRoles(Collection<? extends GrantedAuthority> collection){
    Set<String> rolesSet = new HashSet<>();
    for (GrantedAuthority authority : collection) {
      rolesSet.add(authority.getAuthority());
    }
    return String.join(",", rolesSet);
  }

  public ResponseCookie responseCookieFrom(String jwt) {
    return ResponseCookie.from("token", jwt).path("/").maxAge(24 * 60 * 60)
        .httpOnly(true)
        .secure(true)
        .build();
  }
}
