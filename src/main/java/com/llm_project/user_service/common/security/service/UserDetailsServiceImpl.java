package com.llm_project.user_service.common.security.service;

import com.llm_project.user_service.exceptions.ErrorException;
import com.llm_project.user_service.user.entity.User;
import com.llm_project.user_service.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username){
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found with username: " + username));

    return UserDetailsImpl.build(user);
  }
}
