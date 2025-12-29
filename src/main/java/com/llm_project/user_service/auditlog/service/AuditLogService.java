package com.llm_project.user_service.auditlog.service;

import com.llm_project.user_service.auditlog.dto.AuditLogDTO;
import com.llm_project.user_service.auditlog.entity.LogEntity;

public interface AuditLogService {

  LogEntity saveLog(String userId, String ipAddr, String module, String action, Object oldData, Object newData);

  AuditLogDTO detail(String logNum);
}
