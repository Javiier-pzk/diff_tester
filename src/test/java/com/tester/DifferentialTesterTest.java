package com.tester;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DifferentialTesterTest {

  @Test
  void runLabelTest() {
    DifferentialTester dt = new DifferentialTester("Label.java", "LabelTest.java", "apply");
    dt.run();
  }

  @Test
  void runRegressionExampleTest() {
    DifferentialTester dt = new DifferentialTester(
            "RegressionExample.java", "RegressionExampleTest.java",
            "example123");
    dt.run();
  }
}