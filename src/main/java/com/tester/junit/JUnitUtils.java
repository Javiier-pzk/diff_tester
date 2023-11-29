package com.tester.junit;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import com.tester.exceptions.CompilationError;
import org.jacoco.agent.rt.RT;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.data.SessionInfo;
import org.jacoco.core.tools.ExecFileLoader;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.launcher.listeners.TestExecutionSummary.Failure;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

public class JUnitUtils {

  private static final String CODE_START = "```java\n";
  private static final String CODE_END = "```";
  private static final String REGRESSION = "regression";
  private static final String WORKING = "working";
  private static final String MAIN = "main";
  private static final String TEST = "test";
  private static final String TARGET_DIR = "target/test-classes";
  private final String fileName;

  public JUnitUtils(String fileName) {
    this.fileName = fileName;
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
    int startIndex;
    int codeStartIndex = response.indexOf(CODE_START);
    String codeStartCapital = CODE_START.replace("j", "J");
    int codeStartCapitalIndex = response.indexOf(codeStartCapital);
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
    writeToFile(WORKING, code);
    writeToFile(REGRESSION, code);
  }

  public TestExecutionSummary runWorkingTest() throws CompilationError {
    return runTest(WORKING);
  }

  public TestExecutionSummary runRegressionTest() throws CompilationError{
    return runTest(REGRESSION);
  }

  public String readWorkingProgram() {
    return readProgram(getFilePath(MAIN, WORKING));
  }

  public String readRegressionProgram() {
    return readProgram(getFilePath(MAIN, REGRESSION));
  }

  private void writeToFile(String type, String code) {
    byte[] codeBytes = code.getBytes();
    String packageName = getPackageName(type);
    byte[] packageNameBytes = packageName.getBytes();
    String filePath = getFilePath(TEST, type);
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

  private TestExecutionSummary runTest(String type) throws CompilationError {
    if (!compileTest(type)) {
      throw new CompilationError("Failed to compile " + getFilePath(TEST, type));
    }
    String fullyQualifiedClassName = getQualifiedClassName(type);
    SummaryGeneratingListener listener = new SummaryGeneratingListener();
    Launcher launcher = LauncherFactory.create();
    launcher.registerTestExecutionListeners(listener);
    launcher.execute(LauncherDiscoveryRequestBuilder.request()
            .selectors(selectClass(fullyQualifiedClassName))
            .build());
    return listener.getSummary();
  }

  private void extractTestCoverageInfo(String type) {
    try {
      RT.getAgent().dump(true);
      ExecFileLoader loader = new ExecFileLoader();
      loader.load(new File("target/jacoco.exec"));
      CoverageBuilder coverageBuilder = new CoverageBuilder();
      Analyzer analyzer = new Analyzer(loader.getExecutionDataStore(), coverageBuilder);
      analyzer.analyzeAll(new File(getClassPath(type)));
      List<SessionInfo> sessionInfos = loader.getSessionInfoStore().getInfos();
      for (SessionInfo info : sessionInfos) {
        System.out.println("Session: " + info.getId() + " (" + info.getStartTimeStamp() + " - " + info.getDumpTimeStamp() + ")");
      }
      for (final IClassCoverage cc : coverageBuilder.getClasses()) {
        System.out.println("Line coverage " + cc.getName() + ": " + cc.getLineCounter().getCoveredRatio() * 100 + "%");
        System.out.println("Method coverage " + cc.getName() + ": " + cc.getMethodCounter().getCoveredRatio() * 100 + "%");
        System.out.println("Branch coverage " + cc.getName() + ": " + cc.getBranchCounter().getCoveredRatio() * 100 + "%");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private boolean compileTest(String type) {
    try {
      String classFilePath = getClassPath(type);
      Files.deleteIfExists(Paths.get(classFilePath));
    } catch (IOException e) {
      e.printStackTrace();
    }
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null) {
      return false;
    }
    int compilationResult = compiler.run(null, null, null,
            "-d", TARGET_DIR, getFilePath(TEST, type));
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

  private String getFilePath(String dir, String type) {
    return "src/" + dir + "/java/examples/" + type + "/" + fileName;
  }

  private String getClassPath(String type) {
    return TARGET_DIR + "/examples/" + type + "/" + getBaseName() + ".class";
  }

  private String getQualifiedClassName(String type) {
    return "examples." + type + "." + getBaseName();
  }

  private String getPackageName(String type) {
    return "package examples." + type + ";\n\n";
  }
}
