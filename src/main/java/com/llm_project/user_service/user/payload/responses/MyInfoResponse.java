package com.llm_project.user_service.user.payload.responses;

import com.llm_project.user_service.common.constants.enums.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MyInfoResponse {
  String username;
  String email;
  String fullName;
  UserStatus status;
  String dateOfBirth;
  ZonedDateTime createdDt;
  ZonedDateTime updatedDt;
}
