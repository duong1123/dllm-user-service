package com.llm_project.user_service.auditlog.service.impl;

import com.llm_project.user_service.auditlog.dto.AuditLogDTO;
import com.llm_project.user_service.auditlog.entity.AuditLog;
import com.llm_project.user_service.auditlog.repository.AuditLogRepository;
import com.llm_project.user_service.auditlog.service.AuditLogService;
import com.llm_project.user_service.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditLogServiceImpl implements AuditLogService {

  final UserRepository userRepository;

  final AuditLogRepository auditLogRepository;

  @Override
  public AuditLog saveLog(String userId,String ipAddr, String module, String action, Object oldData, Object newData) {
    try {
      String ip = getIpAddress();

      AuditLog log = new AuditLog();
      log.setUserId(userId);
      log.setModule(module);
      log.setAction(action);
      log.setLogDetails(
          details(oldData, newData)
      );
      log.setIpAddr(ip);

      return auditLogRepository.save(log);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public AuditLogDTO detail(String logNum) {
    return null;
  }

  private String getIpAddress() {
    return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
        .map(ServletRequestAttributes.class::cast)
        .map(ServletRequestAttributes::getRequest)
        .map(request -> request.getHeader(HttpHeaders.CONTENT_LOCATION))
        .orElse(null);
  }

  private Map<String, Object> details(Object oldData, Object newData) {
    var details = new HashMap<String, Object>();
    details.put("oldData", oldData);
    details.put("newData", newData);
    return details;
  }
}
