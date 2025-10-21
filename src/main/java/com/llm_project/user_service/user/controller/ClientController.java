package com.llm_project.user_service.user.controller;

import com.llm_project.user_service.user.payload.requests.ClientInfoUpdateRequest;
import com.llm_project.user_service.user.service.ClientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientController {

  ClientService clientService;

  @GetMapping()
  ResponseEntity<?> getUserInfo() {
    return clientService.clientInfoView();
  }

  @PostMapping()
  ResponseEntity<?> updateUserInfo(@RequestBody ClientInfoUpdateRequest request) {
    return clientService.clientInfoUpdate(request);
  }
}
