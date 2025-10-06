package com.llm_project.user_service.auth.repository;

import com.llm_project.user_service.auth.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {

  Session findByUserId(String userId);
  boolean existsByAccessToken(String accessToken);
}
