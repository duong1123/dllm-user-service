package com.llm_project.user_service.auditlog.repository;

import com.llm_project.user_service.auditlog.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
