package com.tester.processor;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.jacoco.core.analysis.*;
import org.junit.jupiter.api.Test;

class MavenTestExecutorTest {

  @Test
  public void executeTest() {
    String fullyQualifiedTestClassName = "examples.working.RegressionExampleTest";
    String classPath = "target/classes/examples/working/RegressionExample.class";
    try {
      MavenTestExecutionSummary summary = MavenTestExecutor.execute(
              fullyQualifiedTestClassName, classPath);
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
      IClassCoverage cc = summary.getClassCoverage();
      System.out.println("Class name: " + cc.getName());
      System.out.println("Class line coverage: " + cc.getLineCounter().getCoveredRatio() * 100 + "%");
      System.out.println("Class branch coverage: " + cc.getBranchCounter().getCoveredRatio() * 100 + "%");
      System.out.println("Method coverage:");
      for (IMethodCoverage mc : cc.getMethods()) {
        System.out.println("Method name: " + mc.getName());
        System.out.println("Method desc: " + mc.getDesc());
        System.out.println("Method line coverage: " + mc.getLineCounter().getCoveredRatio() * 100 + "%");
        System.out.println("Method branch coverage: " + mc.getBranchCounter().getCoveredRatio() * 100 + "%");
      }
    } catch (MavenInvocationException e) {
      e.printStackTrace();
    }
  }
}