package com.tester.junit;

import com.tester.exceptions.CompilationError;
import org.junit.jupiter.api.Test;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.launcher.listeners.TestExecutionSummary.Failure;

import java.util.*;

class JUnitUtilsTest {

  private final JUnitUtils junitUtils = new JUnitUtils("Label.java", "LabelTest.java", "apply");

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
    junitUtils.extractTest(response);
  }

  @Test
  public void readProgramTest() {
    String program = junitUtils.readWorkingProgram();
    System.out.println(program);
  }

  @Test
  public void extractFailuresTest() {
    try {
      TestExecutionSummary workingSummary = junitUtils.runWorkingTest();
      List<Failure> failures = workingSummary.getFailures();
      String result = junitUtils.extractFailures(failures);
      System.out.println(result);
    } catch (CompilationError e) {
      e.printStackTrace();
    }
  }

  @Test
  public void extractCoverageTest() {
    String coverageInfo = junitUtils.extractWorkingTestCoverageInfo();
    System.out.println(coverageInfo);
  }
}