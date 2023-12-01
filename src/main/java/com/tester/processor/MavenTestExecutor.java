package com.tester.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;

public class MavenTestExecutor {

  private static final Dotenv dotenv = Dotenv.load();
  private static final String MVN_PATH = dotenv.get("MVN_PATH");
  private static final String POM_FILE = "pom.xml";
  private static final String TEST = "test";
  private static final String MVN_ERROR_TAG = "[ERROR]";
  private static final String MVN_INFO_TAG = "[INFO]";
  private static final String TESTS_RUN = "Tests run";
  private static final String RUNNING = "Running";
  private static final String FAILURES = "Failures";

  public static MavenTestExecutionSummary execute(
          String fullyQualifiedClassName) throws MavenInvocationException {
    InvocationRequest request = new DefaultInvocationRequest();
    LinkedList<String> output = new LinkedList<>();
    request.setOutputHandler(output::add);
    request.setPomFile(new File(POM_FILE));
    request.setGoals(Collections.singletonList(TEST));
    Properties properties = new Properties();
    properties.setProperty(TEST, fullyQualifiedClassName);
    request.setProperties(properties);
    Invoker invoker = new DefaultInvoker();
    invoker.setMavenHome(new File(MVN_PATH));
    InvocationResult result = invoker.execute(request);
    boolean isSuccess = result.getExitCode() == 0;
    return getSummary(output, isSuccess);
  }

  private static MavenTestExecutionSummary getSummary(LinkedList<String> output,
                                                      boolean isSuccess) {
    String testClassName = "";
    int totalTests = -1, failureCount = -1, errorCount = -1, skippedCount = -1;
    List<MavenTestFailure> failures = Collections.emptyList();
    while (!output.isEmpty()) {
      String line = output.pollFirst();
      if (line.contains(RUNNING)) {
        String[] parts = line.split(" ");
        testClassName = parts[parts.length - 1];
        continue;
      }
      if (line.contains(TESTS_RUN)) {
        String[] parts = line.split(",");
        totalTests = Integer.parseInt(parts[0].split(":")[1].trim());
        failureCount = Integer.parseInt(parts[1].split(":")[1].trim());
        errorCount = Integer.parseInt(parts[2].split(":")[1].trim());
        skippedCount = Integer.parseInt(parts[3].split(":")[1].trim());
        if (!isSuccess) {
          failures = extractFailures(output, testClassName);
        }
        break;
      }
    }
    return new MavenTestExecutionSummary(
            testClassName, totalTests, failureCount, errorCount, skippedCount, failures);
  }

  private static List<MavenTestFailure> extractFailures(LinkedList<String> output,
                                                        String testClassName) {
    List<List<String>> stackTraces = extractStackTraces(output);
    List<MavenTestFailure> failures = new ArrayList<>();
    int stackTraceIndex = 0;
    while (!output.isEmpty()) {
      String line = output.pollFirst();
      if (line.contains(TESTS_RUN)) {
        break;
      }
      if (line.contains(MVN_INFO_TAG)) {
        continue;
      }
      if (line.contains(MVN_ERROR_TAG)) {
        if (line.contains(FAILURES)) {
          continue;
        }
        String names = line.split(" ")[3];
        String methodNameAndLineNum = names.split("\\.")[1];
        String[] methodNameAndLineNumParts = methodNameAndLineNum.split(":");
        String methodName = methodNameAndLineNumParts[0];
        int lineNumber = Integer.parseInt(methodNameAndLineNumParts[1]);
        List<String> stackTrace = stackTraces.get(stackTraceIndex++);
        failures.add(new MavenTestFailure(testClassName, methodName, lineNumber, stackTrace));
      }
    }
    return failures;
  }

  private static List<List<String>> extractStackTraces(LinkedList<String> output) {
    List<List<String>> stackTraces = new ArrayList<>();
    List<String> currStackTrace = new ArrayList<>();
    while (!output.isEmpty()) {
      String line = output.pollFirst();
      if (line.isEmpty()) {
        stackTraces.add(currStackTrace);
        currStackTrace = new ArrayList<>();
        continue;
      }
      if (line.contains(MVN_ERROR_TAG)) {
        continue;
      }
      if (line.contains(MVN_INFO_TAG)) {
        break;
      }
      currStackTrace.add(line);
    }
    return stackTraces;
  }
}
