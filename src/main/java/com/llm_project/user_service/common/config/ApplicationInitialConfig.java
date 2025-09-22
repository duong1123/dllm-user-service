package com.llm_project.user_service.common.config;

import com.llm_project.user_service.exceptions.ErrorException;
import com.llm_project.user_service.user.entity.Role;
import com.llm_project.user_service.user.entity.User;
import com.llm_project.user_service.user.entity.UserRole;
import com.llm_project.user_service.user.repository.RoleRepository;
import com.llm_project.user_service.user.repository.UserRepository;
import com.llm_project.user_service.user.repository.UserRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitialConfig {

  PasswordEncoder passwordEncoder;
  RoleRepository roleRepository;
  UserRoleRepository userRoleRepository;

  @Bean
  ApplicationRunner applicationRunner(UserRepository userRepository){
    return args -> {
      if (userRepository.findByUsername("admin").isEmpty()){

        User admin = User.builder()
            .username("admin")
            .email("admin@dllm-example.com")
            .passwordHash(passwordEncoder.encode("admin001"))
            .build();
        userRepository.save(admin);

        if (roleRepository.findByRoleName("ADMIN").isEmpty()){
          Role adminRole = new Role();
          adminRole.setRoleName("ADMIN");
          roleRepository.save(adminRole);
        }

        Role adminRole = roleRepository.findByRoleName("ADMIN").orElseThrow(() ->
            new ErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Admin role not found")
        );

        User savedAdmin = userRepository.findByUsername("admin").orElseThrow(() ->
            new ErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Admin creation failed")
        );

        UserRole role = new UserRole();
        role.setRoleId(adminRole.getId());
        role.setUserId(savedAdmin.getId());
        userRoleRepository.save(role);

        log.warn("default admin user created with default password");
      }
    };
  }
}
