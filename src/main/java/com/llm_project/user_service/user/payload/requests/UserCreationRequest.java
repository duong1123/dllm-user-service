package com.llm_project.user_service.user.payload.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
  @Size(min = 6, message = "Username must be at least 6 characters long")
  String username;

  @Size(min = 8, message = "Password must be at least 8 characters long")
  String password;

  @NotBlank(message = "Email cannot be blank")
  @Email(message = "Invalid email format")
  String email;

  @NotBlank(message = "Full name cannot be blank")
  String fullName;

  @NotBlank(message = "Date of birth cannot be blank")
  Date dateOfBirth;
}
