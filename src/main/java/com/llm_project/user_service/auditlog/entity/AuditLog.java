package com.llm_project.user_service.auditlog.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.ZonedDateTime;
import java.util.Map;

@Getter
@Setter

@Entity
@Table  (name = "audit_log")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "log_type")
  String logType;

  @Column(name = "user_id")
  String userId;

  @Column(name = "ip_addr")
  String ipAddr;

  @Column(name = "ip_country")
  String ipCountry;

  @Column(name = "ip_city")
  String ipCity;

  @CreationTimestamp
  @Column(name = "log_dt")
  ZonedDateTime logDt;

  @Column(name = "module")
  String module;

  @Column(name = "action")
  String action;

  @Column(name = "log_details", columnDefinition = "jsonb")
  @Type(JsonBinaryType.class)
  Map<String, Object> logDetails;
}
