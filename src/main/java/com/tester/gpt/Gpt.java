package com.tester;


import java.io.*;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import io.github.cdimascio.dotenv.*;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.*;

public class Gpt {
  private final List<Map<String, String>> messages;
  private final HttpClient httpClient;
  private final String apiKey;


  public Gpt() {
    Dotenv dotenv = Dotenv.load();
    apiKey = dotenv.get("OPENAI_API_KEY");
    httpClient = HttpClients.createDefault();
    messages = new ArrayList<>();
  }

  public void addMessage(String role, String content) {
    Map<String, String> message = new HashMap<>();
    message.put(role, content);
    messages.add(message);
  }

  public String getJsonMessages() {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(messages);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return "";
    }
  }

  public void generate(String prompt, String model) {
    String endpoint = "https://api.openai.com/v1/chat/completions";
    HttpPost httpPost = new HttpPost(endpoint);
    httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);
    addMessage("user", prompt);
    String jsonMessages = getJsonMessages();
    String body = "{\"model\": \"" + model + "\"," + "\"messages\": " + getJsonMessages() + "}";
    httpPost.setEntity(new StringEntity(body, "UTF-8"));

    try {
      HttpResponse response = httpClient.execute(httpPost);
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        String responseString = EntityUtils.toString(entity, "UTF-8");
        System.out.println(responseString);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
