package com.llm_project.user_service.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "session")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Session {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "user_id")
  String userId;

  @Column(name = "access_token")
  String accessToken;

  @Column(name = "refresh_token")
  String refreshToken;

  @Column(name = "create_dt")
  @CreationTimestamp
  ZonedDateTime createDt;

  @Column(name = "update_dt")
  @UpdateTimestamp
  ZonedDateTime updateDt;
}
