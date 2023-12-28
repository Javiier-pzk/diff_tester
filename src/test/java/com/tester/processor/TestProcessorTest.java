package com.tester.processor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.jupiter.api.Test;


class TestProcessorTest {

  private final Map<String, List<Integer>> suspiciousLines = new HashMap<String, List<Integer>>() {{
    put("working", Arrays.asList(25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37));
    put("regression", Arrays.asList(25, 26, 27, 28, 29));
  }};
  private final TestProcessor testProcessor = new TestProcessor("Label.java", "LabelTest.java",
          "apply", suspiciousLines);

  @Test
  void extractTest() {
    String response = "In the buggy version of the class, if `excludes` is not `null` but `label` is not in `excludes`, the function will return true regardless of whether `label` is in `includes`. \n" +
            "\n" +
            "On the other hand, in the working version of the class, even if `label` is not in `excludes`, it will further check if `label` is in `includes`. \n" +
            "\n" +
            "So to differentiate these two versions, we can examine the scenario where `label` is not in `excludes` but also not in `includes`. The working version will return false while the buggy version will return true. Below is such a test case:\n" +
            "\n" +
            "```java\n" +
            "import static org.junit.jupiter.api.Assertions.assertFalse;\n" +
            "import static org.junit.jupiter.api.Assertions.assertTrue;\n" +
            "import org.junit.jupiter.api.Test;\n" +
            "\n" +
            "class LabelTest {\n" +
            "\n" +
            "  @Test\n" +
            "  void testApply() {\n" +
            "    String[] includes = {\"apple\", \"banana\"};\n" +
            "    String[] excludes = {\"cherry\", \"dragonfruit\"};\n" +
            "    String labelNotInBothLists = \"elderberry\";\n" +
            "\n" +
            "    Label labelObject = new Label(includes, excludes);\n" +
            "\n" +
            "    assertFalse(labelObject.apply(labelNotInBothLists), \n" +
            "      \"Apply method should return false for labels not present in both includes and excludes.\");\n" +
            "  }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "In this case, for the correctly functioning class, the call `labelObject.apply(labelNotInBothLists)` should return `false` because `elderberry` is not in `includes`. However, for the buggy version, it will incorrectly return `true`.  \n" +
            "\n" +
            "So this test case will pass for the correctly functioning `Label` class, and fail " +
            "for the buggy version.";
    testProcessor.extractTest(response);
  }

  @Test
  public void readProgramTest() {
    String program = testProcessor.readWorkingProgram();
    System.out.println(program);
  }

  @Test
  public void extractFailuresTest() {
    try {
      MavenTestExecutionSummary workingSummary = testProcessor.runWorkingTest();
      List<MavenTestFailure> failures = workingSummary.getFailures();
      String result = testProcessor.extractFailures(failures);
      System.out.println(result);
    } catch (MavenInvocationException e) {
      e.printStackTrace();
    }
  }
}