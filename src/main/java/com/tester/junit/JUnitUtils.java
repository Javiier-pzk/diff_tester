package com.tester.junit;


import static org.junit.platform.engine.discovery.DiscoverySelectors.selectFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

public class JUnitUtils {

  private static final String CODE_START = "```java\n";
  private static final String CODE_END = "```";
  private static final String EXAMPLES_FILE_PATH = "/src/test/java/examples/";
  private static final String REGRESSION = "regression";
  private static final String WORKING = "working";
  private static final String PACKAGE_NAME = "package examples.";
  private final String workingFilePath;
  private final String regressionFilePath;
  private final String packageNameWorking;
  private final String packageNameRegression;

  public JUnitUtils(String fileName) {
    packageNameWorking = PACKAGE_NAME + WORKING + ";\n\n";
    packageNameRegression = PACKAGE_NAME + REGRESSION + ";\n\n";
    workingFilePath =
            System.getProperty("user.dir") + EXAMPLES_FILE_PATH + WORKING + "/" + fileName;
    regressionFilePath =
            System.getProperty("user.dir") + EXAMPLES_FILE_PATH + REGRESSION + "/" + fileName;
  }

  public void extractTest(String response) {
    int startIndex = response.indexOf(CODE_START);
    if (startIndex == -1) {
      return;
    }
    int endIndex = response.indexOf(CODE_END, startIndex + 1);
    if (endIndex == -1) {
      return;
    }
    String code = response.substring(startIndex + CODE_START.length(), endIndex);
    writeToFile(workingFilePath, packageNameWorking, code);
    writeToFile(regressionFilePath, packageNameRegression, code);
  }

  public TestExecutionSummary runWorkingTest() {
    return runTest(workingFilePath);
  }

  public TestExecutionSummary runRegressionTest() {
    return runTest(regressionFilePath);
  }

  public String readWorkingProgram() {
    return readProgram(workingFilePath);
  }

  public String readRegressionProgram() {
    return readProgram(regressionFilePath);
  }

  private void writeToFile(String filePath, String packageName, String code) {
    byte[] codeBytes = code.getBytes();
    byte[] packageNameBytes = packageName.getBytes();
    try {
      Files.write(Paths.get(filePath), packageNameBytes, StandardOpenOption.CREATE,
              StandardOpenOption.APPEND);
      Files.write(Paths.get(filePath), codeBytes, StandardOpenOption.CREATE,
              StandardOpenOption.APPEND);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private TestExecutionSummary runTest(String filePath) {
    SummaryGeneratingListener listener = new SummaryGeneratingListener();
    Launcher launcher = LauncherFactory.create();
    launcher.registerTestExecutionListeners(listener);
    launcher.execute(LauncherDiscoveryRequestBuilder.request()
            .selectors(selectFile(filePath))
            .build());
    return listener.getSummary();
  }

  private String readProgram(String filePath) {
    try {
      List<String> lines = Files.readAllLines(Paths.get(filePath));
      return String.join(System.lineSeparator(), lines);
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
}
