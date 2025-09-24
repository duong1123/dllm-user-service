package com.llm_project.user_service.common.utils;

import com.llm_project.user_service.auth.entity.Otp;
import com.llm_project.user_service.auth.repository.OtpRepository;
import com.llm_project.user_service.common.security.JwtUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OTPUtils {

  final OtpRepository otpRepository;
  final PasswordEncoder passwordEncoder;
  final JwtUtils jwtUtils;

  public String generateOtp() {
    int otpLength = 6;

    String characters = "0123456789";

    SecureRandom random;
    try{
      random = SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException exception){
      throw new RuntimeException("No secure random available", exception);
    }

    StringBuilder otp = new StringBuilder();
    for (int i = 0; i < otpLength; i++) {
      int index = random.nextInt(characters.length());
      otp.append(characters.charAt(index));
    }

    return otp.toString();
  }

  public boolean isOTPExpired(Otp otp) {
    return false;
  }
}
