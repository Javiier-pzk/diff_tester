package com.tester;

import org.junit.jupiter.api.*;
import java.util.*;

class LLMAgentTest {
  @Test
  void runLabelTest() {
    Map<String, List<Integer>> suspiciousLines = new HashMap<String, List<Integer>>() {{
      put("working", Arrays.asList(25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37));
      put("regression", Arrays.asList(25, 26, 27, 28, 29));
    }};
    LLMAgent agent = new LLMAgent("Label.java",
            "LabelTest.java",
            "apply",
            suspiciousLines);
    agent.run();
  }

  @Test
  void runRegressionExampleTest() {
    Map<String, List<Integer>> suspiciousLines = new HashMap<String, List<Integer>>() {{
      put("working", Collections.singletonList(6));
      put("regression", Collections.singletonList(6));
    }};
    LLMAgent agent = new LLMAgent(
            "RegressionExample.java", "RegressionExampleTest.java",
            "example123", suspiciousLines);
    agent.run();
  }
}