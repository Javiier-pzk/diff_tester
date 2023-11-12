package com.tester.gpt;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GptResponse {
  private final String id;
  private final String object;
  private final int created;
  private final String model;
  private final List<GptChoice> choices;
  private final GptUsage usage;

  @JsonCreator
  public GptResponse(@JsonProperty("id") String id,
                     @JsonProperty("object") String object,
                     @JsonProperty("created") int created,
                     @JsonProperty("model") String model,
                     @JsonProperty("choices") List<GptChoice> choices,
                     @JsonProperty("usage") GptUsage usage) {
    this.id = id;
    this.object = object;
    this.created = created;
    this.model = model;
    this.choices = choices;
    this.usage = usage;
  }

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("object")
  public String getObject() {
    return object;
  }

  @JsonProperty("created")
  public int getCreated() {
    return created;
  }

  @JsonProperty("model")
  public String getModel() {
    return model;
  }

  @JsonProperty("choices")
  public List<GptChoice> getChoices() {
    return choices;
  }

  @JsonProperty("usage")
  public GptUsage getUsage() {
    return usage;
  }

  @Override
  public String toString() {
    return "{\n  id: '" + id + "'," +
            "\n  object: '" + object + "'," +
            "\n  created: " + created + "," +
            "\n  model: '" + model + "'," +
            "\n  choices: " + choices + "," +
            "\n  usage: " + usage +
            "\n}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof GptResponse)) {
      return false;
    }
    GptResponse other = (GptResponse) o;
    return other.id.equals(id) &&
            other.object.equals(object) &&
            other.created == created &&
            other.model.equals(model) &&
            other.choices.equals(choices) &&
            other.usage.equals(usage);
  }
}
