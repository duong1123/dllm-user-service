package com.llm_project.user_service.common.security;

import com.llm_project.user_service.common.security.service.UserDetailsImpl;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.*;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtils {

  @Value("${jwt.private-key}")
  private Resource privateKeyResource;

  @Value("${jwt.public-key}")
  private Resource publicKeyResource;

  @Value("${jwt.expiration.ms}")
  private int jwtExpirationMs;

  @Value("${jwt.refresh.token.expiration.ms}")
  private int jwtRefreshTokenExpirationMs;

  private PrivateKey getPrivateKey() {
    try{
    byte[] keyBytes = privateKeyResource.getInputStream().readAllBytes();
      String privateKeyPem = new String(keyBytes)
          .replace("-----BEGIN PRIVATE KEY-----", "")
          .replace("-----END PRIVATE KEY-----", "")
          .replaceAll("\\s", "");
      byte[] decoded = java.util.Base64.getDecoder().decode(privateKeyPem);
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePrivate(keySpec);

    } catch (Exception e) {
      log.error("Error loading private key: {}", e.getMessage());
      throw new RuntimeException("Error loading private key", e);
    }

  }

  public PublicKey getPublicKey() {
    try {
      byte[] keyBytes = publicKeyResource.getInputStream().readAllBytes();
      String publicKeyPem = new String(keyBytes)
          .replace("-----BEGIN PUBLIC KEY-----", "")
          .replace("-----END PUBLIC KEY-----", "")
          .replaceAll("\\s", "");
      byte[] decoded = java.util.Base64.getDecoder().decode(publicKeyPem);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePublic(keySpec);

    } catch (Exception e) {
      log.error("Error loading public key: {}", e.getMessage());
      throw new RuntimeException("Error loading public key", e);
    }
  }

  public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal, boolean isRefreshToken) {
    String jwt = generateTokenFromUsernameAndRole(userPrincipal.getUsername(),
        userPrincipal.getAuthorities(),
        isRefreshToken);
    return responseCookieFrom(jwt);
  }

  public ResponseCookie generateJwtCookie(String username, Collection<? extends GrantedAuthority> roles, boolean isRefreshToken) {
    String jwt = generateTokenFromUsernameAndRole(username,
        roles,
        isRefreshToken);
    return responseCookieFrom(jwt);
  }

  public String generateTokenFromUsernameAndRole(String username,
                                                  Collection<? extends GrantedAuthority> authorities,
                                                  boolean isRefreshToken) {

    int tokenExpirationMs = jwtExpirationMs;

    PrivateKey privateKey = getPrivateKey();

    if(isRefreshToken)
      tokenExpirationMs = jwtRefreshTokenExpirationMs;

    JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
        .type(JOSEObjectType.JWT)
        .build();

    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
        .subject(username)
        .claim("username", username)
        .claim("roles", buildRoles(authorities))
        .issueTime(new Date())
        .expirationTime(new Date((new Date()).getTime() + tokenExpirationMs))
        .build();

    SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);

    RSASSASigner signer = new RSASSASigner(privateKey);
    try {
      signedJWT.sign(signer);

      return signedJWT.serialize();
      } catch (Exception e) {
        log.error("Error signing JWT: {}", e.getMessage());
        throw new RuntimeException("Error signing JWT", e);
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

  public HttpHeaders HttpHeadersClearCookie() {
    ResponseCookie responseCookieClear = ResponseCookie.from("token",  "").path("/").maxAge(0)
        .httpOnly(true)
        .secure(true)
        .build();

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, responseCookieClear.toString());
    return headers;
  }
}
