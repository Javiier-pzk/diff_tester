package com.tester.gpt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GptUsage {
  private final int promptTokens;
  private final int completionTokens;
  private final int totalTokens;

  @JsonCreator
  public GptUsage(@JsonProperty("prompt_tokens") int promptTokens,
                  @JsonProperty("completion_tokens") int completionTokens,
                  @JsonProperty("total_tokens") int totalTokens) {
    this.promptTokens = promptTokens;
    this.completionTokens = completionTokens;
    this.totalTokens = totalTokens;
  }

  @JsonProperty("prompt_tokens")
  public int getPromptTokens() {
    return promptTokens;
  }

  @JsonProperty("completion_tokens")
  public int getCompletionTokens() {
    return completionTokens;
  }

  @JsonProperty("total_tokens")
  public int getTotalTokens() {
    return totalTokens;
  }

  @Override
  public String toString() {
    return "{ promptTokens: " + promptTokens +
        ", completionTokens: " + completionTokens +
        ", totalTokens: " + totalTokens + " }";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof GptUsage)) {
      return false;
    }
    GptUsage other = (GptUsage) o;
    return other.promptTokens == promptTokens &&
        other.completionTokens == completionTokens &&
        other.totalTokens == totalTokens;
  }
}
