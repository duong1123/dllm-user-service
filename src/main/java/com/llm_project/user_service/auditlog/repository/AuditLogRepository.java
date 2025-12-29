package com.llm_project.user_service.auditlog.repository;

import com.llm_project.user_service.auditlog.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<LogEntity, Long> {
}
