package com.llm_project.user_service.common.constants;

public class ErrorCode {
  public interface AUTH{

  }

  public interface USER{
    String USERNAME_EXISTED = "This username is already taken.";
  }

}
