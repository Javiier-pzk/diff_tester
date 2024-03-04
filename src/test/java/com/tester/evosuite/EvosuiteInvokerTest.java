package com.tester.evosuite;

import org.junit.jupiter.api.Test;

class EvosuiteInvokerTest {

  @Test
  void executeTest() {
    String command = new EvosuiteCommandBuilder()
            .withDefaultProjectCP()
            .withClass("examples.working.Label")
            .withTargetMethod("apply(Ljava/lang/String;)Z")
            .withCriterion("branch")
            .build();
    String res = EvosuiteInvoker.execute(command);
    System.out.println(res);
  }
}
