package com.llm_project.user_service.user.service.Impl;

import com.llm_project.user_service.user.entity.User;
import com.llm_project.user_service.user.mapper.UserMapper;
import com.llm_project.user_service.user.payload.responses.MyInfoResponse;
import com.llm_project.user_service.user.repository.UserRepository;
import com.llm_project.user_service.user.service.ClientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientServiceImpl implements ClientService {

  UserRepository userRepository;

  UserMapper userMapper;

  @Override
  public ResponseEntity<?> clientInfoView() {
    var context = SecurityContextHolder.getContext();
    var username = context.getAuthentication().getName();

    User user = userRepository.findByUsername(username).orElse(null);
    MyInfoResponse myInfoResponse = userMapper.toMyInfoResponse(user);
    return ResponseEntity.ok()
        .body(myInfoResponse);
  }
}
