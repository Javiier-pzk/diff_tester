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

  @Test
  public void extractSubtasksTest() {
    String response = "\"Subtasks to generate JUnit5 tests that can differentiate both versions " +
            "include:\n\n1. Understand the functionality of both methods: " +
            "Understanding how the correct method is supposed to work is vital. " +
            "Examining the broken method and identifying the problem helps in creating effective " +
            "unit tests. It appears that the problem with the buggy version is how it processes a " +
            "label that should be excluded.\n\n2. Prepare a test plan: A test plan should " +
            "include scenarios that cover all possible code paths (i.e., includes, excludes, or " +
            "both are null, label is in includes, label is in excludes etc.)\n\n3. Setup JUnit" +
            " test environment: To run JUnit tests, you'll need to have JUnit5 libraries in " +
            "your classpath. Most modern IDEs support JUnit and you simply need to include JUnit5" +
            " library in your project structure.\n\n4. Create a JUnit5 test " +
            "class: This class will contain the tests specific to the \"Label\" class" +
            ".\n\n5. Create the test methods: Each test method will represent a single test " +
            "case based on the test plan. Each method will call the \"apply\" method of the " +
            "Label class with different parameters and then check the return value using the " +
            "JUnit assert methods.\n\n6. Run the tests on the working version: " +
            "It will ensure that all our test cases are working fine.\n\n7. Run the tests on" +
            " the regression version: Some test cases should fail due to the code issue in " +
            "this version of \"apply\" method.\n\n8. Refactor tests if necessary: If " +
            "there's a test that isn't providing the desired results, it might need to " +
            "be tweaked or replaced with a new, more appropriate test.\"";
    List<String> res = testProcessor.extractSubtasks(response);
    for (String s : res) {
      System.out.println(s);
    }
  }
}