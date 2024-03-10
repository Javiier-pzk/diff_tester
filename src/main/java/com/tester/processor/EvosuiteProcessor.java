package com.tester.processor;

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
  private final String evosuiteFileName;
  private final String evosuiteScaffoldingFileName;

  public EvosuiteProcessor(String fileName,
                           String targetMethod,
                           Map<String, List<Integer>> suspiciousLines) {
    super(fileName, fileName, targetMethod, suspiciousLines);
    evosuiteFileName = fileName.replace(JAVA, ES_TEST + JAVA);
    evosuiteScaffoldingFileName = evosuiteFileName.replace(JAVA, SCAFFOLDING + JAVA);
  }

  public EvosuiteProcessor(String fileName,
                           Map<String, List<Integer>> suspiciousLines) {
    super(fileName, fileName, suspiciousLines);
    evosuiteFileName = fileName.replace(JAVA, ES_TEST + JAVA);
    evosuiteScaffoldingFileName = evosuiteFileName.replace(JAVA, SCAFFOLDING + JAVA);
  }

  @Override
  protected String readProgram(String filepath) {
    StringBuilder sb = new StringBuilder();
    String importQualifiedClassName = IMPORT + getQualifiedClassName(WORKING);
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

  public void extractTest() {
    String testCode = readProgram(getEvosuiteGeneratedFilePath(evosuiteFileName));
    String scaffoldingCode = readProgram(getEvosuiteGeneratedFilePath(evosuiteScaffoldingFileName));
    writeTestToFile(testCode);
    writeScaffoldingCodeToFile(scaffoldingCode);
  }

  @Override
  protected void writeTestToFile(String code) {
    writeToFile(getPackageName(WORKING), code, getFilePath(TEST, WORKING, evosuiteFileName));
    writeToFile(getPackageName(REGRESSION), code, getFilePath(TEST, REGRESSION, evosuiteFileName));
  }

  @Override
  protected String getFilePath(String dir, String type, String fileName) {
    return SRC + dir + EVOSUITE + EXAMPLES + "/" + type + "/" + fileName;
  }

  @Override
  protected String getClassPath(String type, String fileName) {
    return TARGET_DIR + EXAMPLES + "/" + type + "/" + getBaseName(fileName) + CLASS;
  }

  @Override
  protected String getQualifiedClassName(String type) {
    return EXAMPLES + "." + type + "." + getBaseName(testFileName);
  }

  @Override
  protected String getPackageName(String type) {
    return PACKAGE + EXAMPLES + "." + type + ";\n\n";
  }

  private void writeScaffoldingCodeToFile(String code) {
    writeToFile(getPackageName(WORKING), code,
            getFilePath(TEST, WORKING, evosuiteScaffoldingFileName));
    String regressionCode = code.replace(
            getQualifiedClassName(WORKING), getQualifiedClassName(REGRESSION));
    writeToFile(getPackageName(REGRESSION), regressionCode,
            getFilePath(TEST, REGRESSION, evosuiteScaffoldingFileName));
  }

  private String getEvosuiteGeneratedFilePath(String fileName) {
    return EVOSUITE_OUTPUT_DIR + EXAMPLES + "/" + WORKING + "/" + fileName;
  }
}
