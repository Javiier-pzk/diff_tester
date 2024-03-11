package com.tester;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EvoLLMAgentTest {

  @Test
  void runLabelTest() {
    Map<String, List<Integer>> suspiciousLines = new HashMap<String, List<Integer>>() {{
      put("working", Arrays.asList(25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37));
      put("regression", Arrays.asList(25, 26, 27, 28, 29));
    }};
    EvoLLMAgent agent = new EvoLLMAgent("Label.java",
            "apply(Ljava/lang/String;)Z",
            suspiciousLines);
    agent.run();
  }
}