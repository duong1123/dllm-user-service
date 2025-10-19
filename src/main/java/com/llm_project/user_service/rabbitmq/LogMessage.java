package com.llm_project.user_service.rabbitmq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class LogMessage {

  String logType;
  String userId;
  String ipAddr;
  ZonedDateTime logDt;
  String module;
  String action;
  Map<String, Object> logDetails;
}
