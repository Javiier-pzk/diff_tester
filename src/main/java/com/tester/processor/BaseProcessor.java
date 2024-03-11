package com.tester.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.IMethodCoverage;


public abstract class BaseProcessor {

  protected static final String REGRESSION = "regression";
  protected static final String WORKING = "working";
  protected static final String SRC = "src/";
  protected static final String PACKAGE = "package ";
  protected static final String MAIN = "main/java/";
  protected static final String TEST = "test/java/";
  protected static final String CLASS = ".class";
  protected static final String EXAMPLES = "examples";
  protected static final String TARGET_DIR = "target/classes/";
  private static final String CODE_START = "```java\n";
  private static final String CODE_END = "```";

  protected final String testFileName;
  protected final String targetMethod;
  protected final String programFileName;
  protected final Map<String, List<Integer>> suspiciousLines;

  public BaseProcessor(String programFileName,
                       String testFileName,
                       String targetMethod,
                       Map<String, List<Integer>> suspiciousLines) {
    this.programFileName = programFileName;
    this.testFileName = testFileName;
    this.targetMethod = targetMethod;
    this.suspiciousLines = suspiciousLines;
  }

  public BaseProcessor(String programFileName,
                       String testFileName,
                       Map<String, List<Integer>> suspiciousLines) {
    this.programFileName = programFileName;
    this.testFileName = testFileName;
    this.targetMethod = "";
    this.suspiciousLines = suspiciousLines;
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

  public String getBaseName(String fileName) {
    int dotIndex = fileName.lastIndexOf('.');
    return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
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
    writeTestToFile(code);
  }

  public MavenTestExecutionSummary runWorkingTest() throws MavenInvocationException {
    return MavenTestExecutor.execute(
            getQualifiedClassName(WORKING), getClassPath(WORKING, programFileName));
  }

  public MavenTestExecutionSummary runRegressionTest() throws MavenInvocationException {
    return MavenTestExecutor.execute(
            getQualifiedClassName(REGRESSION), getClassPath(REGRESSION, programFileName));
  }

  public String readWorkingProgram() {
    return readProgram(getFilePath(MAIN, WORKING, programFileName));
  }

  public String readRegressionProgram() {
    return readProgram(getFilePath(MAIN, REGRESSION, programFileName));
  }

  public String extractWorkingCoverageInfo(MavenTestExecutionSummary summary) {
    return extractTestCoverageInfo(summary, WORKING);
  }

  public String extractRegressionCoverageInfo(MavenTestExecutionSummary summary) {
    return extractTestCoverageInfo(summary, REGRESSION);
  }

  protected void writeToFile(String packageName, String code, String filePath) {
    byte[] codeBytes = code.getBytes();
    byte[] packageNameBytes = packageName.getBytes();
    try {
      if (Files.exists(Paths.get(filePath))) {
        Files.delete(Paths.get(filePath));
      } else {
        Files.createDirectories(Paths.get(filePath).getParent());
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

  protected String getProgramLine(int lineNum, String filePath) {
    int currLineNum = 1;
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

  protected List<String> getProgramLines(List<Integer> lineNums, String filePath) {
    int currLineNum = 1, i = 0;
    List<String> res = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filePath));
      String line = reader.readLine();
      while (line != null && i < lineNums.size()) {
        int lineNum = lineNums.get(i);
        if (currLineNum == lineNum) {
          res.add(line);
          i++;
        }
        line = reader.readLine();
        currLineNum++;
      }
      return res;
    } catch (IOException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  protected String extractFailures(List<MavenTestFailure> failures, String filePath) {
    StringBuilder sb = new StringBuilder();
    for (MavenTestFailure failure : failures) {
      sb.append("Method: ");
      sb.append(failure.getMethodName());
      sb.append("\n");
      int lineNum = failure.getFailureLineNumber();
      String programLine = getProgramLine(lineNum, filePath);
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

  protected String readProgram(String filePath) {
    try {
      List<String> lines = Files.readAllLines(Paths.get(filePath));
      return String.join(System.lineSeparator(), lines);
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

  private String extractTestCoverageInfo(MavenTestExecutionSummary summary, String type) {
    IClassCoverage cc = summary.getClassCoverage();
    if (cc == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
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
    sb.append("Here are the suspicious lines in the program not covered by any test in the " +
            "test suite:\n");
    Map<Integer, Boolean> lineCoverageStatus = summary.getLineCoverageStatus();
    StringBuilder susLinesSb = new StringBuilder();
    String filePath = getFilePath(MAIN, type, programFileName);
    for (int l : suspiciousLines.get(type)) {
      if (lineCoverageStatus.getOrDefault(l, false)) {
        continue;
      }
      String programLine = getProgramLine(l, filePath);
      susLinesSb.append("Line ");
      susLinesSb.append(l);
      susLinesSb.append(": ");
      susLinesSb.append(programLine);
      susLinesSb.append("\n");
    }
    if (susLinesSb.length() == 0) {
      sb.append("None");
    } else {
      sb.append(susLinesSb);
    }
    return sb.toString();
  }

  protected String getFilePath(String dir, String type, String fileName) {
    return SRC + dir + EXAMPLES + "/" + type + "/" + fileName;
  }

  protected String getClassPath(String type, String fileName) {
    return TARGET_DIR + EXAMPLES + "/" + type + "/" + getBaseName(fileName) + CLASS;
  }

  protected String getQualifiedClassName(String type) {
    return EXAMPLES + "." + type + "." + getBaseName(testFileName);
  }

  protected String getPackageName(String type) {
    return PACKAGE + EXAMPLES + "." + type + ";\n\n";
  }

  public abstract String extractFailures(List<MavenTestFailure> failures);

  protected abstract void writeTestToFile(String code);
}
