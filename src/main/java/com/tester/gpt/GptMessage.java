package com.tester.gpt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GptMessage {
  private final String role;
  private final String content;

  @JsonCreator
  public GptMessage(@JsonProperty("role") String role,
                    @JsonProperty("content") String content) {
    this.role = role;
    this.content = content;
  }

  @JsonProperty("role")
  public String getRole() {
    return role;
  }

  @JsonProperty("content")
  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return "{ role: " + role + ", content: " + content + " }";
  }

  public String toJsonString() {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return "";
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof GptMessage)) {
      return false;
    }
    GptMessage other = (GptMessage) o;
    return other.role.equals(role) && other.content.equals(content);
  }
}
