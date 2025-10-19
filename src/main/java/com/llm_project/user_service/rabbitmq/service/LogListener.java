package com.llm_project.user_service.rabbitmq.service;

import com.llm_project.user_service.auditlog.repository.AuditLogRepository;
import com.llm_project.user_service.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LogListener {

  AuditLogRepository auditLogRepository;
  UserRepository userRepository;

  @RabbitListener(queues = {"${dllm.rabbitmq.queue.log.name}"})
  public void messageHandle(Message message) throws IOException {

  }
}
