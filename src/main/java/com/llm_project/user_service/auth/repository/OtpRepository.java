package com.llm_project.user_service.auth.repository;

import com.llm_project.user_service.auth.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtpRepository extends JpaRepository<Otp, Long> {
  List<Otp> findByOtpUserIdAndIsUsedFalseOrderByUpdateDateAsc(String userId);
}
