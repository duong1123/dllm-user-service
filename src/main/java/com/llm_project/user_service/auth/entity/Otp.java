package com.llm_project.user_service.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "otp")
@Entity
public class Otp implements Serializable {

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code")
  private String code;

  @Column(name = "otp_user_id")
  private String otpUserId;

  @JsonIgnore
  @Column(name = "create_dt")
  @CreationTimestamp
  private ZonedDateTime createDate;

  @JsonIgnore
  @Column(name = "update_dt")
  @UpdateTimestamp
  private ZonedDateTime updateDate;

  @Builder.Default
  @Column(name = "is_used", columnDefinition = "boolean default FALSE")
  private Boolean isUsed = false;
}
