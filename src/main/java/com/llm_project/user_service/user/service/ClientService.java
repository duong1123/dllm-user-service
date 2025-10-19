package com.llm_project.user_service.user.service;

import com.llm_project.user_service.user.payload.requests.ClientInfoUpdateRequest;
import org.springframework.http.ResponseEntity;

public interface ClientService {

  ResponseEntity<?> clientInfoView();

  ResponseEntity<?> clientInfoUpdate(ClientInfoUpdateRequest request);
}
