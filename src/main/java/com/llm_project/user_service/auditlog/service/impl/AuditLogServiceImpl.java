package com.llm_project.user_service.auditlog.service.impl;

import com.llm_project.user_service.auditlog.dto.AuditLogDTO;
import com.llm_project.user_service.auditlog.entity.AuditLog;
import com.llm_project.user_service.auditlog.service.AuditLogService;

public class AuditLogServiceImpl implements AuditLogService {

  @Override
  public AuditLog saveLog(String module, String action, Object oldData, Object newData) {
    return null;
  }

  @Override
  public AuditLog saveUserLog(String module, String action, Object oldData, Object newData, Long userId) {
    return null;
  }

  @Override
  public AuditLogDTO detail(String logNum) {
    return null;
  }
}
