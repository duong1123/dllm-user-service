package com.llm_project.user_service.user.repository;

import com.llm_project.user_service.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  boolean existsByUsername(String username);

  User getUserByUsername(String username);

  Optional<User> findByUsername(String username);

}
