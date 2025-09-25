package com.llm_project.user_service.email.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "email_log")
public class EmailLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "recipient_email")
  private String recipientEmail;

  @Column(name = "subject")
  private String subject;

  @Column(name = "create_dt")
  private ZonedDateTime createDate;
}
