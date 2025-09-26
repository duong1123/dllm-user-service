package com.llm_project.user_service.auth.service;

import com.llm_project.user_service.auth.entity.Otp;
import com.llm_project.user_service.auth.repository.OtpRepository;
import com.llm_project.user_service.common.utils.OTPUtils;
import com.llm_project.user_service.email.service.EmailService;
import com.llm_project.user_service.user.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpService {

  OtpRepository otpRepository;

  OTPUtils otpUtils;

  PasswordEncoder passwordEncoder;

  EmailService emailService;

  @Transactional
  public void sendOtp(User user){

    otpRepository.deleteByOtpUserIdAndIsUsedFalse(user.getId());

    String rawOtp = otpUtils.generateOtp();
    String encodedOtp = passwordEncoder.encode(rawOtp);

    Otp otp = Otp.builder()
        .otpUserId(user.getId())
        .code(encodedOtp)
        .isUsed(false)
        .expireDate(ZonedDateTime.now().plusMinutes(5))
        .build();

    otpRepository.save(otp);

    emailService.sendOtpEmail(user.getEmail(), rawOtp);
  }

  @Transactional
  public boolean verifyOtp(User user, String inputOtp){
    Optional<Otp> otpOpt =
        otpRepository.findTopByOtpUserIdAndIsUsedFalseOrderByExpireDateDesc(user.getId());

    if (otpOpt.isEmpty()) return false;

    Otp otp = otpOpt.get();

    if (otp.getExpireDate().isBefore(ZonedDateTime.now())) {
      otp.setIsUsed(true);
      otpRepository.save(otp);
      return false;
    }

    if (passwordEncoder.matches(inputOtp, otp.getCode())) {
      otp.setIsUsed(true);
      otpRepository.save(otp);
      return true;
    }

    return false;
  }

  @Scheduled(cron = "0 0 * * * *")
  @Transactional
  public void cleanupExpiredOtps() {
    otpRepository.deleteExpired(ZonedDateTime.now());
  }
}
