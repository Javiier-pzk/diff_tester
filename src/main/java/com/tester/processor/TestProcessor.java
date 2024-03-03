package com.tester.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.*;


public class TestProcessor extends BaseProcessor {

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

    public TestProcessor(String programFileName, String testFileName, String targetMethod, Map<String, List<Integer>> suspiciousLines) {
        super(programFileName, testFileName, targetMethod, suspiciousLines);
    }

    public TestProcessor(String programFileName,
                         String testFileName,
                         Map<String, List<Integer>> suspiciousLines) {
        super(programFileName, testFileName, suspiciousLines);
    }

    @Override
    protected void writeTestToFile(String code) {
        writeToFile(WORKING, code);
        writeToFile(REGRESSION, code);
    }

    @Override
    String getFilePath(String dir, String type, String fileName) {
        return SRC + dir + EXAMPLES + "/" + type + "/" + fileName;
    }

    @Override
    String getClassPath(String type, String fileName) {
        return TARGET_DIR + EXAMPLES + "/" + type + "/" + getBaseName(fileName) + CLASS;
    }

    @Override
    String getQualifiedClassName(String type) {
        return EXAMPLES + "." + type + "." + getBaseName(testFileName);
    }

    @Override
    String getPackageName(String type) {
        return PACKAGE + EXAMPLES + "." + type + ";\n\n";
    }

  public String getExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf('.');
    return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
  }


}
