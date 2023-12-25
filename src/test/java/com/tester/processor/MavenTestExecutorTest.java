package com.tester.processor;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.jupiter.api.Test;

class MavenTestExecutorTest {

  @Test
  public void executeTest() {
    String fullyQualifiedTestClassName = "examples.working.RegressionExampleTest";
    try {
      MavenTestExecutionSummary summary = MavenTestExecutor.execute(fullyQualifiedTestClassName);
      System.out.println("class name: " + summary.getClassName());
      System.out.println("Tests passed: " + summary.getTestsSucceededCount());
      System.out.println("Tests failed: " + summary.getTestsFailedCount());
      System.out.println("Tests skipped: " + summary.getTestsSkippedCount());
      System.out.println("Tests aborted: " + summary.getTestsAbortedCount());
      System.out.println("Failure size: " + summary.getFailures().size());
      for (MavenTestFailure failure : summary.getFailures()) {
        System.out.println("Failure: " + failure.getClassName() + "." + failure.getMethodName() + " at line " + failure.getFailureLineNumber());
        System.out.println("Exception: " + failure.getStackTrace());
      }
    } catch (MavenInvocationException e) {
      e.printStackTrace();
    }
  }
}