package com.llm_project.user_service.user.service;

import com.llm_project.user_service.user.payload.requests.ClientInfoUpdateRequest;
import com.llm_project.user_service.user.payload.requests.UserCreationRequest;
import org.springframework.http.ResponseEntity;

public interface ClientService {

  ResponseEntity<?> createUser(UserCreationRequest request);

  ResponseEntity<?> clientInfoView();

  ResponseEntity<?> clientInfoUpdate(ClientInfoUpdateRequest request);

}
