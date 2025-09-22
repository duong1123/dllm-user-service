package com.llm_project.user_service.user.mapper;

import com.llm_project.user_service.user.entity.User;
import com.llm_project.user_service.user.payload.requests.UserCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUser(UserCreationRequest request);
}
