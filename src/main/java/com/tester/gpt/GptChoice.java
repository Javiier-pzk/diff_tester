package com.tester.gpt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GptChoice {
  private final int index;
  private final GptMessage message;
  private final String finishReason;
  private final String logProbs;

  @JsonCreator
  public GptChoice(@JsonProperty("index") int index,
                   @JsonProperty("message") GptMessage message,
                   @JsonProperty("finish_reason") String finishReason,
                   @JsonProperty("logprobs") String logProbs) {
    this.index = index;
    this.message = message;
    this.finishReason = finishReason;
    this.logProbs = logProbs;
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
    return finishReason;
  }

  @JsonProperty("logprobs")
  public String getLogProbs() {
    return logProbs;
  }

  @Override
  public String toString() {
    return "{ index: " + index + ", message: " + message +
            ", logprobs: '" + logProbs + "', finish_reason: '" + finishReason + "' + }";
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
            other.finishReason.equals(finishReason);
  }
}
