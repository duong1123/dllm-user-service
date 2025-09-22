package com.llm_project.user_service.user.repository;

import com.llm_project.user_service.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByRoleName(String name);
}
