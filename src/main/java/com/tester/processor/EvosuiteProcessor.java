package com.tester.processor;

import com.tester.evosuite.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class EvosuiteProcessor extends BaseProcessor {

  private static final String EVOSUITE_OUTPUT_DIR = "evosuite-tests/";
  private static final String EVOSUITE = "evosuite/";
  private static final String ES_TEST = "_ESTest";
  private static final String SCAFFOLDING = "_scaffolding";
  private static final String TEST = "test/";
  private static final String JAVA = ".java";
  private static final String IMPORT = "import ";
  private static final String BRANCH = "branch";
  private final String scaffoldingFileName;

  public EvosuiteProcessor(String fileName,
                           String targetMethod,
                           Map<String, List<Integer>> suspiciousLines) {
    super(fileName, fileName.replace(JAVA, ES_TEST + JAVA), targetMethod, suspiciousLines);
    scaffoldingFileName = testFileName.replace(JAVA, SCAFFOLDING + JAVA);
  }

  public EvosuiteProcessor(String fileName,
                           Map<String, List<Integer>> suspiciousLines) {
    super(fileName,  fileName.replace(JAVA, ES_TEST + JAVA), suspiciousLines);
    scaffoldingFileName = testFileName.replace(JAVA, SCAFFOLDING + JAVA);
  }

  @Override
  protected String readProgram(String filepath) {
    StringBuilder sb = new StringBuilder();
    String importQualifiedClassName = IMPORT + getProgramQualifiedClassName(WORKING);
    try {
      BufferedReader br = Files.newBufferedReader(Paths.get(filepath));
      String line;
      while ((line = br.readLine()) != null) {
        if (line.contains(PACKAGE) || line.contains(importQualifiedClassName)) {
          continue;
        }
        sb.append(line).append("\n");
      }
      return sb.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

  @Override
  public String extractFailures(List<MavenTestFailure> failures) {
    return extractFailures(failures, getFilePath(TEST, WORKING, testFileName));
  }

  public void generateTest() {
    EvosuiteCommandBuilder builder = new EvosuiteCommandBuilder()
            .withDefaultProjectCP()
            .withClass(getQualifiedClassName(WORKING))
            .withCriterion(BRANCH);
    if (!targetMethod.isEmpty()) {
      builder.withTargetMethod(targetMethod);
    }
    EvosuiteInvoker.execute(builder.build());
  }

  public void extractTest() {
    String testCode = readProgram(getEvosuiteGeneratedFilePath(testFileName));
    String scaffoldingCode = readProgram(getEvosuiteGeneratedFilePath(scaffoldingFileName));
    writeTestToFile(testCode);
    writeScaffoldingCodeToFile(scaffoldingCode);
  }

  @Override
  protected void writeTestToFile(String code) {
    writeToFile(getPackageName(WORKING), code, getFilePath(TEST, WORKING, testFileName));
    writeToFile(getPackageName(REGRESSION), code, getFilePath(TEST, REGRESSION, testFileName));
  }

  @Override
  protected String getFilePath(String dir, String type, String fileName) {
    return SRC + dir + EVOSUITE + EXAMPLES + "/" + type + "/" + fileName;
  }

  private void writeScaffoldingCodeToFile(String code) {
    writeToFile(getPackageName(WORKING), code,
            getFilePath(TEST, WORKING, scaffoldingFileName));
    String regressionCode = code.replace(
            getProgramQualifiedClassName(WORKING),
            getProgramQualifiedClassName(REGRESSION));
    writeToFile(getPackageName(REGRESSION), regressionCode,
            getFilePath(TEST, REGRESSION, scaffoldingFileName));
  }

  private String getEvosuiteGeneratedFilePath(String fileName) {
    return EVOSUITE_OUTPUT_DIR + EXAMPLES + "/" + WORKING + "/" + fileName;
  }

  private String getProgramQualifiedClassName(String type) {
    return EXAMPLES + "." + type + "." + getBaseName(programFileName);
  }
}
