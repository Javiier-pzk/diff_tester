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

  public static String getLLMAgentPrompt(String workingProgram,
                                         String regressionProgram,
                                         String targetMethod) {
    return "In the following prompt, I am going to provide you with two Java methods, " +
            "one of it is the working implementation while the other has a regression error " +
            "introduced. Your tasks is to generate JUnit5 tests that can differentiate both " +
            "versions. A successful response would be if you are able to generate a test suite " +
            "such that all tests in the test suite pass when run against the working " +
            "version and at least one test fails when run against the regression version. I will" +
            " provide you with the class that you need to test. In particular, I want you to " +
            "focus on the " + targetMethod + " method in the class. " +
            "Here is the working version of the class:\n\n" + workingProgram +
            "\n\nHere is the buggy version of the class:\n\n" + regressionProgram +
            "\n\n Since this can be quite a challenging task, I first want you to decompose this " +
            "task into smaller subtasks that you think are essential to solving this problem. " +
            "After you have identified the subtasks, we will work on each of them one by one, " +
            "breaking each subtask down into even smaller subtasks if required. We will " +
            "continue this process until we have a set of steps that you think you can " +
            "implement to solve the problem. You can start by listing the subtasks required " +
            "to solve this problem.";
  }

  public static String getSubtaskPrompt(String subtask) {
    return "This is a subtask that you have identified: " + subtask + "\n\n. " +
            "Please answer either \"yes\" or \"no\" to the following question. Does this subtask " +
            "need further decomposition?";
  }

  public static String getNoFurtherDecompositionPrompt(String subtask) {
    return "This is a subtask that you have identified that requires no further decomposition: " +
            subtask + "\n\n. Please answer \"yes\" if this subtask if implementable by you" +
            "in code and \"no\" if this subtask requires follow up action from the end user.";
  }

  public static String getFurtherDecompositionNeededPrompt(String subtask) {
    return "This is a subtask that you have identified that requires further decomposition: " +
            subtask + "\n\nPlease decompose this subtask into even smaller subtasks that you " +
            "think are essential to solving this problem. " +
            "After you have identified the subtasks, we will work on each of them one by one, " +
            "breaking each subtask down into even smaller subtasks if required";
  }

  public static String getImplementablePrompt(String subtask) {
    return "This is a subtask that you have identified that is implementable by you: " +
            subtask + "\n\n. Please provide the necessary implementation in code required to " +
            "solve this subtask.";
  }

  public static String getFullImplementationPrompt(String testFileName) {
    return "Based on our previous discussion where you broke down the task of differential " +
            "testing into various smaller subtasks and provided the implementation for each " +
            "subtask wherever possible, can you combine all the implementations you have " +
            "provided for all implementable subtasks and come up with a complete and compilable " +
            "implementation that would solve the differential testing problem? Take note that in " +
            "your complete implementation, you do not need to provide any package statement, or " +
            "any import statements related to the test class. Moreover, you are to name your " +
            "implementation class \"" + testFileName + "\" and you do not need to use the fully " +
            "qualified name of the test class, you can just use the base name of " +
            "the class as provided the working and regression versions. However, for all other " +
            "dependencies, please include all necessary import statements required " +
            "for your implementation to compile";
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
            "\n\nBased on this additional information, please generate a new test suite that " +
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
