package com.llm_project.user_service.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "role_name", length = 128,  nullable = false)
  private String roleName;

  @Column(name = "description", length = 256)
  private String roleDescription;
}
