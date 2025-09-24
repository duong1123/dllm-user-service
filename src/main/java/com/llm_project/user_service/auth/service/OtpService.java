package com.llm_project.user_service.auth.service;

import com.llm_project.user_service.user.entity.User;

public interface OtpService {

  public void sendOtp(User user);

  public boolean verifyOtp(User user, String inputOtp);
}
