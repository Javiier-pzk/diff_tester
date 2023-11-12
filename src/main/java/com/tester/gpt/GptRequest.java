package com.tester.gpt;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GptRequest {
  private final List<GptMessage> messages;
  private final String model;

  public GptRequest(List<GptMessage> messages, Model model) {
    this.messages = messages;
    this.model = model.toString();
  }

  @JsonProperty("messages")
  public List<GptMessage> getMessages() {
    return messages;
  }

  @JsonProperty("model")
  public String getModel() {
    return model;
  }

  @Override
  public String toString() {
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
    if (!(o instanceof GptRequest)) {
      return false;
    }
    GptRequest other = (GptRequest) o;
    return other.messages.equals(messages) && other.model.equals(model);
  }
}
