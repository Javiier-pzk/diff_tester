package com.tester.gpt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Gpt {
  private final HttpClient httpClient;
  private final String apiKey;
  private final List<GptMessage> messages;

  public Gpt() {
    Dotenv dotenv = Dotenv.load();
    apiKey = dotenv.get("OPENAI_API_KEY");
    httpClient = HttpClients.createDefault();
    messages = new ArrayList<>();
  }

  public void generate(String prompt, Model model) {
    String endpoint = "https://api.openai.com/v1/chat/completions";
    HttpPost httpPost = new HttpPost(endpoint);
    httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);
    GptMessage promptMessage = new GptMessage("user", prompt);
    messages.add(promptMessage);
    GptRequest request = new GptRequest(messages, model);
    httpPost.setEntity(new StringEntity(request.toString(), "UTF-8"));

    try {
      HttpResponse response = httpClient.execute(httpPost);
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        String responseString = EntityUtils.toString(entity, "UTF-8");
        GptResponse gptResponse = new ObjectMapper().readValue(responseString, GptResponse.class);
        List<GptChoice> choices = gptResponse.getChoices();
        GptChoice choice = choices.get(0);
        messages.add(choice.getMessage());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getLastMessage() {
    return messages.get(messages.size() - 1).getContent();
  }
}
