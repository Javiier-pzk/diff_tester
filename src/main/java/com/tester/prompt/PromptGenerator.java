package com.tester.prompt;

import java.util.List;
import com.tester.junit.JUnitUtils;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.launcher.listeners.TestExecutionSummary.Failure;

public class PromptGenerator {

  public static String getInitialPrompt(String programFileName, String targetMethod) {
    JUnitUtils junitUtils = new JUnitUtils(programFileName);
    String workingProgram = junitUtils.readWorkingProgram();
    String regressionProgram = junitUtils.readRegressionProgram();
    return "In the following prompt, I am going to provide you with two Java methods, " +
            "one of it is the working implementation while the other has a regression error " +
            "introduced. Your tasks is to generate JUnit5 tests that can differentiate both " +
            "versions. A successful response would be if you are able to generate a test that " +
            "passes for the working implementation and fails for the buggy implementation. I will" +
            " provide you with the class that you need to test. In particular, I want you to " +
            "focus on the " + targetMethod + " method in the class. " +
            "Here is the working version of the class:\n\n" + workingProgram +
            "\n\nHere is the buggy version of the class:\n\n" + regressionProgram +
            "\n\nWith this information, generate a JUnit5 test that when run on both versions, " +
            "produces different results, i.e., passes for the working version and fails for the " +
            "regression version. You do not have to provide any package statement or import " +
            "statements for the test class";
  }

  public static String getFailurePrompt(TestExecutionSummary workingSummary) {
    long workingFailed = workingSummary.getTotalFailureCount();
    if (workingFailed > 0) {
      return getTestsFailedInWorkingPrompt(workingSummary);
    }
    return getNoTestsFailedInRegressionPrompt();
  }

  public static String getCompileErrorPrompt(Exception e) {
    String exception = JUnitUtils.extractException(e);
    return "The JUnit test suite generated in the previous response produces a compilation error " +
            "when ran against the test class. This is the stack trace of the error:\n\n"
            + exception + "\n\nBased on this information, please provide a new response after " +
            "fixing the errors in the code";
  }

  private static String getTestsFailedInWorkingPrompt(TestExecutionSummary workingSummary) {
    long workingFailed = workingSummary.getTotalFailureCount();
    List<Failure> failures = workingSummary.getFailures();
    String failuresString = JUnitUtils.extractFailures(failures);
    return "In the JUnit test suite generated in the previous response, " + workingFailed +
            " tests failed when ran against the working version of the test class.\n" +
            "This is the summary of the failures in the test suite:\n\n" + failuresString +
            "\n\nBased on this information, please generate a new test suite after fixing the " +
            "existing errors";
  }

  private static String getNoTestsFailedInRegressionPrompt() {
    return "In the JUnit test suite generated in the previous response, 0 tests failed when ran " +
            "against the regression version of the test class. This means that the test suite " +
            "generated in the previous response is not able to differentiate between the working " +
            "and regression versions of the test class. Please modify the test suite such that" +
            "it passes when ran against the working version and fails when ran against the " +
            "regression version.";
  }
}
