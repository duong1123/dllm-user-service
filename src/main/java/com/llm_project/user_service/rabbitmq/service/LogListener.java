package com.llm_project.user_service.rabbitmq.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.llm_project.user_service.auditlog.entity.LogEntity;
import com.llm_project.user_service.auditlog.repository.AuditLogRepository;
import com.llm_project.user_service.rabbitmq.LogMessage;
import com.llm_project.user_service.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LogListener {

  AuditLogRepository auditLogRepository;
  UserRepository userRepository;
  ObjectMapper objectMapper;

  @RabbitListener(queues = {"${dllm.rabbitmq.queue.log.name}"})
  public void messageHandle(Message message) throws IOException {
    try{
      var messageDTO = objectMapper.readValue(message.getBody(), LogMessage.class);

      LogEntity log = new LogEntity();
      log.setLogType(messageDTO.getLogType());
      log.setUserId(messageDTO.getUserId());
      log.setIpAddr(messageDTO.getIpAddr());
      log.setLogDt(messageDTO.getLogDt());
      log.setModule(messageDTO.getModule());
      log.setAction(messageDTO.getAction());
      log.setLogDetails(messageDTO.getLogDetails());

      auditLogRepository.save(log);
    } catch (Exception e) {
      log.error("LogListener error,", e);
    }
  }
}
