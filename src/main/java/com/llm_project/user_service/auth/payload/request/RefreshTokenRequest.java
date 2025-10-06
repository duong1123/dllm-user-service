package com.llm_project.user_service.auth.payload.request;

import lombok.Data;

@Data
public class RefreshTokenRequest {

  String refreshToken;
}
