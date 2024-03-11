package com.tester.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.*;


public class TestProcessor extends BaseProcessor {

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
}
