package com.llm_project.user_service.user.entity;

import com.llm_project.user_service.common.constants.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "email", length = 128, unique = true, nullable = false)
  private String email;

  @Column(name = "username", length = 128, unique = true, nullable = false)
  private String username;

  @Column(name = "password_hash", length = 256, nullable = false)
  private String passwordHash;

  @Column(name = "status")
  private UserStatus status;

  @Builder.Default
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_role",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @Column(name = "created_dt", updatable = false)
  private ZonedDateTime createdDt;

  @Column(name = "updated_dt")
  private ZonedDateTime updatedDt;

  @Column(name = "full_name", length = 128)
  private String fullName;

  @Column(name = "date_of_birth")
  private Date dateOfBirth;
}
