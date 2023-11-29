package com.tester.prompt;

public class PromptGenerator {

  public static String getInitialPrompt(String workingProgram,
                                        String regressionProgram,
                                        String targetMethod) {
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
            "regression version. In the generated test suite, you do not have to provide any " +
            "package statement or import statements related to the test class. However, for all " +
            "other dependencies, please include all necessary import statements required " +
            "for the test suite to compile";
  }

  public static String getCompileErrorPrompt(String exception) {
    return "The JUnit test suite generated in the previous response produces a compilation error " +
            "when ran against the test class. This is the stack trace of the error:\n\n"
            + exception + "\n\nBased on this information, please generate a new test suite with " +
            "no compilation errors. In the new test suite, you do not have to provide any " +
            "package statement or import statements related to the test class. However, for all " +
            "other dependencies, please include all necessary import statements required " +
            "for the test suite to compile";
  }

  public static String getTestsFailedInWorkingPrompt(long workingFailed, String failuresString) {
    return "In the JUnit test suite generated in the previous response, " + workingFailed +
            " tests failed when ran against the working version of the test class.\n" +
            "This is the summary of the failures in the test suite:\n\n" + failuresString +
            "\n\nBased on this information, please generate a new test suite that passes when " +
            "ran against the working version of the test class. In the new test suite, you do " +
            "not have to provide any package statement or import statements related to the test " +
            "class. However, for all other dependencies, please include all necessary import " +
            "statements required for the test suite to compile";
  }

  public static String getNoTestsFailedInRegressionPrompt() {
    return "In the JUnit test suite generated in the previous response, 0 tests failed when ran " +
            "against the regression version of the test class. This means that the test suite " +
            "generated in the previous response is not able to differentiate between the working " +
            "and regression versions of the test class. I will you some extra information on why " +
            "the buggy version causes a bug: " + getSuspiciousLinesPrompt() + " Based on this " +
            "additional information, please generate a new test suite that contains test cases " +
            "that factor in this extra information such that it passes when " +
            "ran against the working version and fails when ran against the regression version. " +
            "In the new test suite, you do not have to provide any package statement or import " +
            "statements related to the test class. However, for all other dependencies, " +
            "please include all necessary import statements required for the test suite to compile";
  }

  private static String getSuspiciousLinesPrompt() {
    return "The buggy code uses \"== -1\" when determining whether label exists in \"excludes\". " +
            "However, Arrays.binarySearch() returns a negative number when no element is found, " +
            "not just -1. This can lead to some labels that should be excluded not being excluded" +
            " properly.";
  }
}
