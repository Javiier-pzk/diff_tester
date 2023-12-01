package com.tester.processor;

import java.util.List;

public class MavenTestFailure {

  private final String testClassName;
  private final String testMethodName;
  private final int testFailureLineNumber;
  private final List<String> stackTrace;

  public MavenTestFailure(String testClassName,
                          String testMethodName,
                          int testFailureLineNumber,
                          List<String> stackTrace) {
    this.testClassName = testClassName;
    this.testMethodName = testMethodName;
    this.testFailureLineNumber = testFailureLineNumber;
    this.stackTrace = stackTrace;
  }

  public String getMethodName() {
    return testMethodName;
  }

  public String getClassName() {
    return testClassName;
  }

  public int getFailureLineNumber() {
    return testFailureLineNumber;
  }

  public List<String> getStackTrace() {
    return stackTrace;
  }
}
