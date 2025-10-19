package com.llm_project.user_service.user.payload.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientInfoUpdateRequest {
  String email;
  String fullName;
  String dateOfBirth;
}
