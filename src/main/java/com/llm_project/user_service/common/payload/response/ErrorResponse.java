package com.llm_project.user_service.common.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ErrorResponse {
  @JsonProperty("code")
  private String code;

  @JsonProperty("value")
  private String value;

  @Builder.Default
  @JsonProperty("errors")
  private List<Map<String, String>> errors = new ArrayList<>();
}
