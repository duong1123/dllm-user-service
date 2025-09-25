package com.llm_project.user_service.email.repository;

import com.llm_project.user_service.email.entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailLog, Long> {
}
