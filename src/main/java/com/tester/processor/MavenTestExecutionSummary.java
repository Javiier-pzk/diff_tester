package com.tester.processor;

import org.jacoco.core.analysis.*;

import java.util.List;

public class MavenTestExecutionSummary {
  private final String testClassName;
  private final int totalTestsCount;
  private final int testsFailedCount;
  private final int testsAbortedCount;
  private final int testsSkippedCount;
  private final List<MavenTestFailure> failures;
  private final IClassCoverage classCoverage;

  public MavenTestExecutionSummary(String testClassName,
                                   int totalTestsCount,
                                   int testsFailedCount,
                                   int testsAbortedCount,
                                   int testsSkippedCount,
                                   List<MavenTestFailure> failures,
                                   IClassCoverage classCoverage) {
    this.testClassName = testClassName;
    this.totalTestsCount = totalTestsCount;
    this.testsFailedCount = testsFailedCount;
    this.testsAbortedCount = testsAbortedCount;
    this.testsSkippedCount = testsSkippedCount;
    this.failures = failures;
    this.classCoverage = classCoverage;
  }

  public String getClassName() {
    return testClassName;
  }

  public int getTestsSucceededCount() {
    return totalTestsCount - testsFailedCount - testsAbortedCount - testsSkippedCount;
  }

  public int getTestsFailedCount() {
    return testsFailedCount;
  }

  public int getTestsAbortedCount() {
    return testsAbortedCount;
  }

  public int getTestsSkippedCount() {
    return testsSkippedCount;
  }

  public int getTestsFoundCount() {
    return totalTestsCount;
  }

  public List<MavenTestFailure> getFailures() {
    return failures;
  }

  public IClassCoverage getClassCoverage() {
    return classCoverage;
  }
}
