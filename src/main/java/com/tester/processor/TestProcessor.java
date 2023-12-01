package com.tester.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.jacoco.agent.rt.RT;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.IMethodCoverage;
import org.jacoco.core.tools.ExecFileLoader;


public class TestProcessor {

  private static final String CODE_START = "```java\n";
  private static final String CODE_END = "```";
  private static final String REGRESSION = "regression";
  private static final String WORKING = "working";
  private static final String SRC = "src/";
  private static final String PACKAGE = "package ";
  private static final String MAIN = "main/java/";
  private static final String TEST = "test/java/";
  private static final String CLASS = ".class";
  private static final String EXAMPLES = "examples";
  private static final String TARGET_DIR = "target/classes";
  private static final String JACOCO_EXEC_FILE_PATH = "target/jacoco.exec";
  private final String programFileName;
  private final String testFileName;
  private final String targetMethod;

  public TestProcessor(String programFileName, String testFileName, String targetMethod) {
    this.programFileName = programFileName;
    this.testFileName = testFileName;
    this.targetMethod = targetMethod;
  }

  public TestProcessor(String programFileName, String testFileName) {
    this.programFileName = programFileName;
    this.testFileName = testFileName;
    this.targetMethod = "";
  }

  public String extractException(Throwable e) {
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

  public String extractFailures(List<MavenTestFailure> failures) {
    StringBuilder sb = new StringBuilder();
    for (MavenTestFailure failure : failures) {
      sb.append("Method: ");
      sb.append(failure.getMethodName());
      sb.append("\n");
      int lineNum = failure.getFailureLineNumber();
      String programLine = getFailedLine(lineNum);
      sb.append("Line ");
      sb.append(lineNum);
      sb.append(": ");
      sb.append(programLine);
      sb.append("\n");
      List<String> stackTrace = failure.getStackTrace();
      int limit = Math.min(stackTrace.size(), 10);
      sb.append("Exception:\n");
      for (int i = 0; i < limit; i++) {
        sb.append(stackTrace.get(i));
        sb.append("\n");
      }
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

  public MavenTestExecutionSummary runWorkingTest() throws MavenInvocationException {
    return MavenTestExecutor.execute(getQualifiedClassName(WORKING));
  }

  public MavenTestExecutionSummary runRegressionTest() throws MavenInvocationException {
    return MavenTestExecutor.execute(getQualifiedClassName(REGRESSION));
  }

  public String readWorkingProgram() {
    return readProgram(getFilePath(MAIN, WORKING, programFileName));
  }

  public String readRegressionProgram() {
    return readProgram(getFilePath(MAIN, REGRESSION, programFileName));
  }

  public String extractWorkingTestCoverageInfo() {
    return extractTestCoverageInfo(WORKING);
  }

  public String extractRegressionTestCoverageInfo() {
    return extractTestCoverageInfo(REGRESSION);
  }

  private void writeToFile(String type, String code) {
    byte[] codeBytes = code.getBytes();
    String packageName = getPackageName(type);
    byte[] packageNameBytes = packageName.getBytes();
    String filePath = getFilePath(TEST, type, testFileName);
    try {
      if (Files.exists(Paths.get(filePath))) {
        Files.delete(Paths.get(filePath));
      }
      FileChannel channel = FileChannel.open(Paths.get(filePath), StandardOpenOption.CREATE,
              StandardOpenOption.APPEND);
      channel.write(ByteBuffer.wrap(packageNameBytes));
      channel.write(ByteBuffer.wrap(codeBytes));
      channel.force(true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getFailedLine(int lineNum) {
    int currLineNum = 1;
    String filePath = getFilePath(TEST, WORKING, testFileName);
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filePath));
      String line = reader.readLine();
      while (line != null && currLineNum < lineNum) {
        line = reader.readLine();
        currLineNum++;
      }
      return line;
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

  private String extractTestCoverageInfo(String type) {
    try {
      RT.getAgent().dump(true);
      ExecFileLoader loader = new ExecFileLoader();
      loader.load(new File(JACOCO_EXEC_FILE_PATH));
      CoverageBuilder coverageBuilder = new CoverageBuilder();
      Analyzer analyzer = new Analyzer(loader.getExecutionDataStore(), coverageBuilder);
      analyzer.analyzeAll(new File(getClassPath(type, programFileName)));
      StringBuilder sb = new StringBuilder();
      for (final IClassCoverage cc : coverageBuilder.getClasses()) {
        sb.append("Class name: ");
        sb.append(cc.getName());
        sb.append("\n");
        sb.append("Line coverage: ");
        sb.append(cc.getLineCounter().getCoveredRatio() * 100);
        sb.append("%");
        sb.append("\n");
        sb.append("Method coverage: ");
        sb.append(cc.getMethodCounter().getCoveredRatio() * 100);
        sb.append("%");
        sb.append("\n\n");
        Collection<IMethodCoverage> methods = cc.getMethods();
        for (IMethodCoverage method : methods) {
          if (targetMethod != null && !targetMethod.equals(method.getName())) {
            continue;
          }
          sb.append("Method: ");
          sb.append(method.getName());
          sb.append(method.getDesc());
          sb.append("\n");
          sb.append("Line coverage: ");
          sb.append(method.getLineCounter().getCoveredRatio() * 100);
          sb.append("%");
          sb.append("\n");
          sb.append("Branch coverage: ");
          sb.append(method.getBranchCounter().getCoveredRatio() * 100);
          sb.append("%");
          sb.append("\n\n");
        }
      }
      return sb.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
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

  private String getBaseName(String fileName) {
    int dotIndex = fileName.lastIndexOf('.');
    return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
  }

  private String getFilePath(String dir, String type, String fileName) {
    return SRC + dir + EXAMPLES + "/" + type + "/" + fileName;
  }

  private String getClassPath(String type, String fileName) {
    return TARGET_DIR + EXAMPLES + "/" + type + "/" + getBaseName(fileName) + CLASS;
  }

  private String getQualifiedClassName(String type) {
    return EXAMPLES + "." + type + "." + getBaseName(testFileName);
  }

  private String getPackageName(String type) {
    return PACKAGE + EXAMPLES + "." + type + ";\n\n";
  }
}
