package com.tester.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.*;


public class TestProcessor extends BaseProcessor {

  private static final String EVOSUITE_TESTS_DIR = "evosuite-tests/";
  public TestProcessor(String programFileName, String testFileName, String targetMethod, Map<String, List<Integer>> suspiciousLines) {
    super(programFileName, testFileName, targetMethod, suspiciousLines);
  }

  public TestProcessor(String programFileName,
                       String testFileName,
                       Map<String, List<Integer>> suspiciousLines) {
    super(programFileName, testFileName, suspiciousLines);
  }

  @Override
  public String extractFailures(List<MavenTestFailure> failures) {
    return extractFailures(failures, getFilePath(TEST, WORKING, testFileName));
  }

  @Override
  protected void writeTestToFile(String code) {
    writeToFile(getPackageName(WORKING), code, getFilePath(TEST, WORKING, testFileName));
    writeToFile(getPackageName(REGRESSION), code, getFilePath(TEST, REGRESSION, testFileName));
  }

  @Override
  protected String getFilePath(String dir, String type, String fileName) {
    return SRC + dir + EXAMPLES + "/" + type + "/" + fileName;
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

  public String getExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf('.');
    return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
  }

  public List<String> extractSubtasks(String input) {
    String regex = "(?m)^\\d+\\.\\s+(.*)$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(input);

    List<String> extractedItems = new ArrayList<>();
    while (matcher.find()) {
      extractedItems.add(matcher.group(1).trim());
    }
    return extractedItems;
  }

  public String readEvosuiteTestSuite(String testSuitePath) {
    return readProgram(testSuitePath);
  }

  private String getEvosuiteTestSuitePath(String fileName) {
    return EVOSUITE_TESTS_DIR + EXAMPLES + fileName;
  }
}
