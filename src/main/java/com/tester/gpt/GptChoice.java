package com.tester.gpt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GptChoice {
  private final int index;
  private final GptMessage message;
  private final String finish_reason;

  @JsonCreator
  public GptChoice(@JsonProperty("index") int index,
                   @JsonProperty("message") GptMessage message,
                   @JsonProperty("finish_reason") String finish_reason) {
    this.index = index;
    this.message = message;
    this.finish_reason = finish_reason;
  }

  @JsonProperty("index")
  public int getIndex() {
    return index;
  }

  @JsonProperty("message")
  public GptMessage getMessage() {
    return message;
  }

  @JsonProperty("finish_reason")
  public String getFinishReason() {
    return finish_reason;
  }

  @Override
  public String toString() {
    return "{ index: " + index + ", message: " + message +
            ", finish_reason: '" + finish_reason + "' }";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof GptChoice)) {
      return false;
    }
    GptChoice other = (GptChoice) o;
    return other.index == index &&
            other.message.equals(message) &&
            other.finish_reason.equals(finish_reason);
  }
}
