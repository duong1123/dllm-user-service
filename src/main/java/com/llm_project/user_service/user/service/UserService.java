package com.llm_project.user_service.user.service;

import com.llm_project.user_service.user.entity.User;
import com.llm_project.user_service.user.payload.requests.UserCreationRequest;

public interface UserService {
  public User createUser(UserCreationRequest request);
}
