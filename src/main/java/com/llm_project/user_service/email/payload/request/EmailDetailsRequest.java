package com.llm_project.user_service.email.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class EmailDetailsRequest {

  private String fromEmail;
  private String toEmail;
  private String subject;
  private String body;
  private List<String> emailList;
}
