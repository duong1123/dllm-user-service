package com.llm_project.user_service.user.mapper;

import com.llm_project.user_service.user.entity.User;
import com.llm_project.user_service.user.payload.requests.ClientInfoUpdateRequest;
import com.llm_project.user_service.user.payload.requests.UserCreationRequest;
import com.llm_project.user_service.user.payload.responses.UserInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User toUserFromCreationRequest(UserCreationRequest request);

  UserInfoResponse toUserInfoResponse(User user);

  void toUserFromClientUpdateRequest(@MappingTarget User user , ClientInfoUpdateRequest request);
}
