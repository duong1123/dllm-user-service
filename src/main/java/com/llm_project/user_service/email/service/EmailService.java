package com.llm_project.user_service.email.service;

import com.llm_project.user_service.email.payload.request.EmailDetailsRequest;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender javaMailSender;
  private final Configuration freemarkerConfig;

  @Value("${email.from}")
  String emailSys;

  private void sendEmail(EmailDetailsRequest request, String path, Map<String, Object> model) {
    try {
      Template template = freemarkerConfig.getTemplate(path);

      StringWriter writer = new StringWriter();
      template.process(model, writer);
      String htmlContent = writer.toString();

      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

      helper.setFrom(request.getFromEmail());
      helper.setTo(request.getToEmail());
      if (request.getEmailList() != null && !request.getEmailList().isEmpty()) {
        helper.setCc(request.getEmailList().toArray(new String[0]));
      }
      helper.setSubject(request.getSubject());
      helper.setText(htmlContent, true); // true = HTML

      javaMailSender.send(mimeMessage);
      log.info("Email sent to {}", request.getToEmail());

    } catch (IOException | TemplateException | MessagingException e) {
      log.error("Failed to send email to {}: {}", request.getToEmail(), e.getMessage());
      throw new RuntimeException("Failed to send email", e);
    }
  }

  public void sendOtpEmail(String toEmail, String otpCode) {
    EmailDetailsRequest request = new EmailDetailsRequest();
    request.setFromEmail(emailSys);
    request.setToEmail(toEmail);
    request.setSubject("Your OTP Code");

    Map<String, Object> model = new HashMap<>();
    model.put("otp", otpCode);

    sendEmail(request, "otp-template.ftl", model);
  }

}
