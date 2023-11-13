package com.tester.prompt;

import com.tester.junit.*;

public class PromptGenerator {

  public static String getInitialPrompt(String programFileName, String methodName) {
    JUnitUtils junitUtils = new JUnitUtils(programFileName);
    String workingProgram = junitUtils.readWorkingProgram();
    String regressionProgram = junitUtils.readRegressionProgram();
    return "In the following prompt, I am going to provide you with two Java methods, " +
            "one of it is the working implementation while the other has a regression error " +
            "introduced. Your tasks is to generate JUnit5 tests that can differentiate both " +
            "versions. A successful response would be if you are able to generate a test that " +
            "passes for the working implementation and fails for the buggy implementation. I will" +
            " provide you with the class that you need to test. In particular, I want you to " +
            "focus on the "+ methodName + " method in the class. Here is the working version of " +
            "the class:\n" + workingProgram +
            "\n. Here is the buggy version of the class:\n" + regressionProgram +
            "\nWith this information, generate me a JUnit5 test that fails for the buggy version " +
            "but passes for the working version";
  }

  public static String getCompileOrRuntimeErrorPrompt() {
    return "";
  }

  public static String getRegressionFailurePrompt() {
    return "";
  }
}
