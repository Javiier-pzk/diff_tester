package com.tester.gpt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GptError {
  private final String message;
  private final String type;
  private final String param;
  private final String code;

  @JsonCreator
  public GptError(@JsonProperty("message") String message,
                  @JsonProperty("type") String type,
                  @JsonProperty("param") String param,
                  @JsonProperty("code") String code) {
    this.message = message;
    this.type = type;
    this.param = param;
    this.code = code;
  }

  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("param")
  public String getParam() {
    return param;
  }

  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    return "{\n    message: " + message + "," +
            "\n    type: " + type + "," +
            "\n    param: " + param + "," +
            "\n    code: " + code + "," +
            "\n  }";
  }
}
