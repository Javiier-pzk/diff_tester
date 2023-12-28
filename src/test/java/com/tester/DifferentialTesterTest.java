package com.tester;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DifferentialTesterTest {

  @Test
  void runLabelTest() {
    Map<String, List<Integer>> suspiciousLines = new HashMap<String, List<Integer>>() {{
      put("working", Arrays.asList(25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37));
      put("regression", Arrays.asList(25, 26, 27, 28, 29));
    }};
    DifferentialTester dt = new DifferentialTester("Label.java",
            "LabelTest.java",
            "apply",
            suspiciousLines);
    dt.run();
  }

  @Test
  void runRegressionExampleTest() {
    Map<String, List<Integer>> suspiciousLines = new HashMap<String, List<Integer>>() {{
      put("working", Collections.singletonList(6));
      put("regression", Collections.singletonList(6));
    }};
    DifferentialTester dt = new DifferentialTester(
            "RegressionExample.java", "RegressionExampleTest.java",
            "example123", suspiciousLines);
    dt.run();
  }
}