package com.llm_project.user_service.user.service.Impl;

import com.llm_project.user_service.common.constants.ErrorCode;
import com.llm_project.user_service.exceptions.ErrorException;
import com.llm_project.user_service.user.entity.User;
import com.llm_project.user_service.user.mapper.UserMapper;
import com.llm_project.user_service.user.payload.requests.ClientInfoUpdateRequest;
import com.llm_project.user_service.user.payload.responses.UserInfoResponse;
import com.llm_project.user_service.user.repository.UserRepository;
import com.llm_project.user_service.user.service.ClientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    UserInfoResponse userInfoResponse = userMapper.toUserInfoResponse(user);
    return ResponseEntity.ok()
        .body(userInfoResponse);
  }

  @Override
  @Transactional
  public ResponseEntity<?> clientInfoUpdate(ClientInfoUpdateRequest request) {
    var context = SecurityContextHolder.getContext();
    var username = context.getAuthentication().getName();

    User user = userRepository.findByUsername(username).orElse(null);

    if (user == null) {
      throw new ErrorException(HttpStatus.BAD_REQUEST, ErrorCode.USER.USER_NOT_FOUND);
    }

    userMapper.toUserFromClientUpdateRequest(user, request);
    userRepository.save(user);

    UserInfoResponse userInfoResponse = userMapper.toUserInfoResponse(user);
    return ResponseEntity.ok()
        .body(userInfoResponse);
  }

}
