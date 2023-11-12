package com.tester.gpt;

public enum Model {
  GPT4_1106_PREVIEW("gpt-4-1106-preview"),
  GPT4_VISION_PREVIEW("gpt-4-vision-preview"),
  GPT4("gpt-4"),
  GPT4_32K("gpt-4-32k"),
  GPT4_0613("gpt-4-0613"),
  GPT4_32K_0613("gpt-4-32k-0613"),
  GPT35_TURBO_1106("gpt-3.5-turbo-1106"),
  GPT35_TURBO("gpt-3.5-turbo"),
  GPT35_TURBO_16K("gpt-3.5-turbo-16k"),
  GPT35_TURBO_INSTRUCT("gpt-3.5-turbo-instruct");

  private final String model;

  Model(String model) {
    this.model = model;
  }

  public String toString() {
    return model;
  }
}
