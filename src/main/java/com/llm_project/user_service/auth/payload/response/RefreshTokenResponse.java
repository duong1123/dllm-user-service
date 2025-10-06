package com.llm_project.user_service.auth.payload.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RefreshTokenResponse {
  String token;
  List<String> roles;
}
