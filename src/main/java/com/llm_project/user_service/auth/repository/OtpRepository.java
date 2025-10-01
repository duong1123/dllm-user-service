package com.llm_project.user_service.auth.repository;

import com.llm_project.user_service.auth.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {

  void deleteByOtpUserIdAndIsUsedFalse(String userId);

  Optional<Otp> findTopByOtpUserIdAndIsUsedFalseOrderByExpireDateDesc(String userId);

  @Modifying
  @Query(value = "DELETE FROM Otp o WHERE o.expire_dt < :now", nativeQuery = true)
  void deleteExpired(@Param("now") ZonedDateTime now);
}
