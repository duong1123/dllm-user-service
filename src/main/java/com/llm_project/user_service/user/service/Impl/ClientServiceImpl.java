package com.llm_project.user_service.user.service.Impl;

import com.llm_project.user_service.common.constants.ErrorCode;
import com.llm_project.user_service.common.constants.enums.UserStatus;
import com.llm_project.user_service.exceptions.ErrorException;
import com.llm_project.user_service.user.entity.Role;
import com.llm_project.user_service.user.entity.User;
import com.llm_project.user_service.user.entity.UserRole;
import com.llm_project.user_service.user.mapper.UserMapper;
import com.llm_project.user_service.user.payload.requests.ClientInfoUpdateRequest;
import com.llm_project.user_service.user.payload.requests.UserCreationRequest;
import com.llm_project.user_service.user.payload.responses.UserInfoResponse;
import com.llm_project.user_service.user.repository.RoleRepository;
import com.llm_project.user_service.user.repository.UserRepository;
import com.llm_project.user_service.user.repository.UserRoleRepository;
import com.llm_project.user_service.user.service.ClientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientServiceImpl implements ClientService {

  UserRepository userRepository;

  UserMapper userMapper;

  UserRoleRepository userRoleRepository;

  PasswordEncoder passwordEncoder;

  RoleRepository roleRepository;

  private final String DEFAULT_ROLE = "USER";

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

    // TODO: Audit log for client info update
  }

  @Override
  @Transactional
  public ResponseEntity<?> createUser(UserCreationRequest request) {
    if(userRepository.existsByUsername(request.getUsername())) {
      throw new ErrorException(HttpStatus.BAD_REQUEST, ErrorCode.USER.USERNAME_EXISTED);
    }

    User user = userMapper.toUserFromCreationRequest(request);
    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    user.setStatus(UserStatus.PENDING);

    userRepository.save(user);

    User savedUser = userRepository.findByUsername(request.getUsername()).orElseThrow(() ->
        new ErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found after creation")
    );

    assignRoleToUser(savedUser);

    userRepository.save(savedUser);

    //TODO: Audit log for user creation

    return ResponseEntity.status(HttpStatus.CREATED)
        .body("User created successfully");
  }

  private void assignRoleToUser(User user) {

    Role defaultRole = roleRepository.findByRoleName(DEFAULT_ROLE).orElseThrow(() ->
        new ErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Default role not found")
    );
    UserRole userRole = new UserRole();
    userRole.setUserId(user.getId());
    userRole.setRoleId(defaultRole.getId());

    userRoleRepository.save(userRole);
  }

}
