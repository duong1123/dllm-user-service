package com.llm_project.user_service.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityUtils {
  @Bean
  PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder(10);
  }
}
