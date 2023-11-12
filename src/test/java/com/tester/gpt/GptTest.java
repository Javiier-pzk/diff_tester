package com.tester.gpt;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

class GptTest {

  @Test
  void generateTest() {
    Gpt gpt = new Gpt();
    String prompt = "Say this is a test";
    gpt.generate(prompt, Model.GPT35_TURBO);
  }

  @Test
  void generateResponse() {
    String jsonString = "{ \"id\": \"chatcmpl-8K3lWJUK2TU1xjgOMIqTp3iz0Jm9y\", \"object\": " +
            "\"chat.completion\", \"created\": 1699791726, \"model\": \"gpt-3.5-turbo-0613\", " +
            "\"choices\": [{ \"index\": 0, \"message\": { \"role\": \"assistant\", \"content\": " +
            "\"This is a test.\" }, \"finish_reason\": \"stop\" }], \"usage\": { " +
            "\"prompt_tokens\": 12, \"completion_tokens\": 5, \"total_tokens\": 17 }}";
    try {
      GptResponse r = new ObjectMapper().readValue(jsonString, GptResponse.class);
      System.out.println(r);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void generateRequest() {
    GptMessage message = new GptMessage("user", "This is a test.");
    List<GptMessage> messages = new ArrayList<>();
    messages.add(message);
    GptRequest request = new GptRequest(messages, Model.GPT35_TURBO);
    try {
      System.out.println(request.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}