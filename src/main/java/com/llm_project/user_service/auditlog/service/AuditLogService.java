package com.llm_project.user_service.auditlog.service;

import com.llm_project.user_service.auditlog.dto.AuditLogDTO;
import com.llm_project.user_service.auditlog.entity.AuditLog;

public interface AuditLogService {

  AuditLog saveLog(String module, String action, Object oldData, Object newData);

  AuditLog saveUserLog(String module, String action, Object oldData, Object newData, Long userId);

  AuditLogDTO detail(String logNum);
}
