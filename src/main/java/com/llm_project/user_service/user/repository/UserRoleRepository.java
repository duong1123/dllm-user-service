package com.llm_project.user_service.user.repository;

import com.llm_project.user_service.user.entity.Role;
import com.llm_project.user_service.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {

  @Query(value = "SELECT r.name AS role_name " +
      "FROM user u JOIN user_role ur ON u.id = ur.user_id " +
      "JOIN role r ON ur.role_id = r.id " +
      "WHERE u.username = :username",
      nativeQuery = true)
  List<String>findRoleNamesByUsername(String username);
}
