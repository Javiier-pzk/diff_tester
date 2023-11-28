package com.tester.junit;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import com.tester.exceptions.CompilationError;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.launcher.listeners.TestExecutionSummary.Failure;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

public class JUnitUtils {

  private static final String CODE_START = "```java\n";
  private static final String CODE_START_CAPITAL = "```Java\n";
  private static final String CODE_END = "```";
  private static final String EXAMPLES_FILE_PATH = "src/test/java/examples/";
  private static final String REGRESSION = "regression";
  private static final String WORKING = "working";
  private static final String PACKAGE_NAME = "package examples.";
  private static final String TARGET_DIR = "target/test-classes";
  private final String fileName;
  private final String workingFilePath;
  private final String regressionFilePath;
  private final String packageNameWorking;
  private final String packageNameRegression;

  public JUnitUtils(String fileName) {
    this.fileName = fileName;
    packageNameWorking = PACKAGE_NAME + WORKING + ";\n\n";
    packageNameRegression = PACKAGE_NAME + REGRESSION + ";\n\n";
    workingFilePath = EXAMPLES_FILE_PATH + WORKING + "/" + fileName;
    regressionFilePath = EXAMPLES_FILE_PATH + REGRESSION + "/" + fileName;
  }

  public static String extractException(Throwable e) {
    StackTraceElement[] stackTraces = e.getStackTrace();
    int limit = Math.min(stackTraces.length, 10);
    StringBuilder sb = new StringBuilder();
    sb.append("Exception:\n");
    sb.append(e);
    for (int i = 0; i < limit; i++) {
      sb.append(stackTraces[i].toString());
      sb.append("\n");
    }
    return sb.toString();
  }

  public static String extractFailures(List<Failure> failures) {
    StringBuilder sb = new StringBuilder();
    for (Failure failure : failures) {
      TestIdentifier identifier = failure.getTestIdentifier();
      sb.append("Method: ");
      sb.append(identifier.getDisplayName());
      sb.append("\n");
      Throwable exception = failure.getException();
      if (exception != null) {
        sb.append(extractException(exception));
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  public void extractTest(String response) {
    int startIndex = 0;
    int codeStartIndex = response.indexOf(CODE_START);
    int codeStartCapitalIndex = response.indexOf(CODE_START_CAPITAL);
    if (codeStartIndex == -1 && codeStartCapitalIndex == -1) {
      return;
    }
    if (codeStartIndex != -1) {
      startIndex = codeStartIndex;
    } else {
      startIndex = codeStartCapitalIndex;
    }
    int endIndex = response.indexOf(CODE_END, startIndex + 1);
    if (endIndex == -1) {
      return;
    }
    String code = response.substring(startIndex + CODE_START.length(), endIndex);
    writeToFile(workingFilePath, packageNameWorking, code);
    writeToFile(regressionFilePath, packageNameRegression, code);
  }

  public TestExecutionSummary runWorkingTest() throws CompilationError {
    return runTest(workingFilePath, WORKING);
  }

  public TestExecutionSummary runRegressionTest() throws CompilationError{
    return runTest(regressionFilePath, REGRESSION);
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
      if (Files.exists(Paths.get(filePath))) {
        Files.delete(Paths.get(filePath));
      }
      Files.write(Paths.get(filePath), packageNameBytes, StandardOpenOption.CREATE,
              StandardOpenOption.APPEND);
      Files.write(Paths.get(filePath), codeBytes, StandardOpenOption.CREATE,
              StandardOpenOption.APPEND);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private TestExecutionSummary runTest(String filePath, String type) throws CompilationError {
    if (!compileTest(filePath, type)) {
      throw new CompilationError("Failed to compile " + fileName + " (" + type + ")");
    }
    String fullyQualifiedClassName = "examples." + type + "." + getBaseName();
    SummaryGeneratingListener listener = new SummaryGeneratingListener();
    Launcher launcher = LauncherFactory.create();
    launcher.registerTestExecutionListeners(listener);
    launcher.execute(LauncherDiscoveryRequestBuilder.request()
            .selectors(selectClass(fullyQualifiedClassName))
            .build());
    return listener.getSummary();
  }

  private boolean compileTest(String filePath, String type) {
    try {
      String classFilePath = TARGET_DIR + "/examples/" + type + "/" + getBaseName() + ".class";
      Files.deleteIfExists(Paths.get(classFilePath));
    } catch (IOException e) {
      e.printStackTrace();
    }
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null) {
      return false;
    }
    int compilationResult = compiler.run(null, null, null,
            "-d", TARGET_DIR, filePath);
    return compilationResult == 0;
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

  private String getBaseName() {
    int dotIndex = fileName.lastIndexOf('.');
    return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
  }
}
