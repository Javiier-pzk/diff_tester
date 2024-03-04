package com.tester.evosuite;

import com.tester.conf.Conf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvosuiteGeneratorTest {

  @Test
  void generateWithCmdTest() {
    EvosuiteGenerator evosuiteGenerator = new EvosuiteGenerator(Conf.EVOSUITE_JAR, Conf.TARGET_EVOSUITE_TESTS_PATH);
    String res = evosuiteGenerator.generateWithCmd(
            System.getProperty("user.dir"),
            "examples.working.Label",
            "apply(Ljava/lang/String;)Z\""
    );
    System.out.println(res);
  }
}