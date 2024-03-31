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

  @Test
  void runRegressionExampleTest() {
    Map<String, List<Integer>> suspiciousLines = new HashMap<String, List<Integer>>() {{
      put("working", Collections.singletonList(6));
      put("regression", Collections.singletonList(6));
    }};
    EvoLLMAgent agent = new EvoLLMAgent(
            "RegressionExample.java",
            "example123(I)I",
            suspiciousLines);
    agent.run();
  }

  @Test
  void runStackTest() {
    Map<String, List<Integer>> suspiciousLines = new HashMap<String, List<Integer>>() {{
      put("working", Collections.singletonList(19));
      put("regression", Collections.singletonList(19));
    }};
    EvoLLMAgent agent = new EvoLLMAgent(
            "StackT.java",
            "pop()Ljava/lang/Object;",
            suspiciousLines);
    agent.run();
  }

  @Test
  void runLinkedListTest() {
    Map<String, List<Integer>> suspiciousLines = new HashMap<String, List<Integer>>() {{
      put("working", Collections.singletonList(16));
      put("regression", Collections.singletonList(12));
    }};
    EvoLLMAgent agent = new EvoLLMAgent(
            "LinkedList.java",
            "get(I)Ljava/lang/Object;",
            suspiciousLines);
    agent.run();
  }
}