package com.tester.evosuite;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class EvosuiteCommandBuilderTest {

  @Test
  void buildCommandTest() {
    String command = new EvosuiteCommandBuilder()
            .withDefaultProjectCP()
            .withClass("examples.working.Label")
            .withTargetMethod("apply(Ljava/lang/String;)Z")
            .withCriterion("branch")
            .build();

    assertEquals("java -jar libs/evosuite-shaded-1.0.7-SNAPSHOT.jar -projectCP target/classes " +
            "-class examples.working.Label -Dtarget_method \"apply(Ljava/lang/String;)Z\" " +
            "-criterion branch" , command);
  }
}