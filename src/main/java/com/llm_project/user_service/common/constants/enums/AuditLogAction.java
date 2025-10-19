package com.llm_project.user_service.common.constants.enums;

public enum AuditLogAction {
  ACCOUNT_CREATE("Account Created"),
  ACCOUNT_UPDATE("Account Updated"),
  CHANGE_PASSWORD("Password Changed"),
  ;


  private final String description;

  AuditLogAction(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return description;
  }

  public String getLog() {
    return description;
  }
}
