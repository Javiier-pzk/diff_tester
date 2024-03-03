package com.tester.processor;

import java.util.List;
import java.util.Map;


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

}
