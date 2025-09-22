package com.llm_project.user_service.user.service.Impl;

import com.llm_project.user_service.common.constants.ErrorCode;
import com.llm_project.user_service.common.constants.enums.UserStatus;
import com.llm_project.user_service.exceptions.ErrorException;
import com.llm_project.user_service.user.entity.Role;
import com.llm_project.user_service.user.entity.User;
import com.llm_project.user_service.user.entity.UserRole;
import com.llm_project.user_service.user.mapper.UserMapper;
import com.llm_project.user_service.user.payload.requests.UserCreationRequest;
import com.llm_project.user_service.user.repository.RoleRepository;
import com.llm_project.user_service.user.repository.UserRepository;
import com.llm_project.user_service.user.repository.UserRoleRepository;
import com.llm_project.user_service.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

  UserRepository userRepository;
  RoleRepository roleRepository;
  UserRoleRepository userRoleRepository;
  UserMapper userMapper;
  PasswordEncoder passwordEncoder;

  private final String DEFAULT_ROLE = "USER";

  @Transactional
  public User createUser(UserCreationRequest request) {
    if(userRepository.existsByUsername(request.getUsername())) {
      throw new ErrorException(HttpStatus.BAD_REQUEST, ErrorCode.USER.USERNAME_EXISTED);
    }

    User user = userMapper.toUser(request);
    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    user.setVerified(false);
    user.setStatus(UserStatus.PENDING);

    userRepository.save(user);

    User savedUser = userRepository.findByUsername(request.getUsername()).orElseThrow(() ->
        new ErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found after creation")
    );

    assignRoleToUser(savedUser);

    return userRepository.save(savedUser);
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
