package com.tester.evosuite;

import org.junit.jupiter.api.Test;

class EvosuiteGeneratorTest {

  @Test
  void generateWithCmdTest() {
    String res = EvosuiteGenerator.generateWithCmd(
            "examples.working.Label",
            "apply(Ljava/lang/String;)Z"
    );
    System.out.println(res);
  }
}