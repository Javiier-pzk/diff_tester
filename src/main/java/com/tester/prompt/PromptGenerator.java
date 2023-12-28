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
            "\n\nWith this information, generate a JUnit5 test suite such that all tests in the " +
            "test suite pass when run against the working version of the program and at " +
            "least one test fails when the same test suite is run against the regression " +
            "version. In the generated test suite, you do not " +
            "have to provide any package statement or import statements related to the test " +
            "class. Moreover, in the test code, you do not need to use the fully qualified name " +
            "of the test class, you can just use the base name of the class as provided the " +
            "working and regression versions. However, for all other dependencies, please " +
            "include all necessary import statements required for the test suite to compile";
  }

  public static String getCompileErrorPrompt(String failuresString) {
    // will clean this up this later
    return "In the JUnit test suite generated in the previous response, there is a compilation " +
            "error when against the working version of the test class.\n" +
            "This is the summary of the compilation errors in the test suite:\n\n" +
            failuresString + "\n\nBased on this information, please generate a new test " +
            "suite that fixes this compilation error and passes when ran against " +
            "the working version of the test class. In the new test suite, you do not have to " +
            "provide any package statement or import statements related to the test class. " +
            "Moreover, in the test code, you do not need to use the fully qualified name of " +
            "the test class, you can just use the base name of the class as provided the " +
            "working version. However, for all other dependencies, please include all necessary " +
            "import statements required for the test suite to compile";
  }

  public static String getTestsFailedInWorkingPrompt(long workingFailed, String failuresString) {
    return "In the JUnit test suite generated in the previous response, " + workingFailed +
            " tests failed when ran against the working version of the test class.\n" +
            "This is the summary of the failures in the test suite:\n\n" + failuresString +
            "\n\nBased on this information, please generate a new test suite such that all tests " +
            "passes when run against the working version of the program and at least one test " +
            "fails when run against the regression version. In the new test suite, you do " +
            "not have to provide any package statement or import statements related to the test " +
            "class. Moreover, in the test code, you do not need to use the fully qualified name " +
            "of the test class, you can just use the base name of the class as provided the " +
            "working version. However, for all other dependencies, please include all necessary " +
            "import statements required for the test suite to compile";
  }

  public static String getNoTestsFailedInRegressionPrompt(String coverage) {
    return "In the JUnit test suite generated in the previous response, 0 tests failed when ran " +
            "against the regression version of the test class. This means that the test suite " +
            "generated in the previous response is not able to differentiate between the working " +
            "and regression versions of the test class. I will provide you the coverage " +
            "information of the test suite as well as any suspicious lines of the program that " +
            "the test suite did not cover: \n" + coverage +
            "\nBased on this additional information, please generate a new test suite that " +
            "covers all uncovered suspicious lines in the program (if any) for the method in " +
            "question by increasing the line or branch coverage (whichever applicable). All " +
            "tests in the new test suite should also pass when run against the working version " +
            "of the program and at least one test should fail when the same test suite is run " +
            "against the regression version. In the new test suite, you do not have to provide " +
            "any package statement or import statements related to the test class. " +
            "Moreover, in the test code, you do not need to use the fully qualified name of " +
            "the test class, you can just use the base name of the class as provided the " +
            "regression version. However, for all other dependencies, please include all " +
            "necessary import statements required for the test suite to compile";
  }
}
