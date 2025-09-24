package com.llm_project.user_service.auth.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerifyRequest {

  String username;
  String otp;
}
