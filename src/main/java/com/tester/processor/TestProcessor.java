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
    protected void writeTest2File(String code) {
        writeToFile(BaseProcessor.getWORKING(), code);
        writeToFile(BaseProcessor.getREGRESSION(), code);
    }

    @Override
    String getFilePath(String dir, String type, String fileName) {
        return BaseProcessor.getSRC() + dir + BaseProcessor.getEXAMPLES() + "/" + type + "/" + fileName;
    }

    @Override
    String getClassPath(String type, String fileName) {
        return BaseProcessor.getTargetDir() + BaseProcessor.getEXAMPLES() + "/" + type + "/" + getBaseName(fileName) + BaseProcessor.getCLASS();
    }

    @Override
    String getQualifiedClassName(String type) {
        return BaseProcessor.getEXAMPLES() + "." + type + "." + getBaseName(super.getTestFileName());
    }

    @Override
    String getPackageName(String type) {
        return BaseProcessor.getPACKAGE() + BaseProcessor.getEXAMPLES() + "." + type + ";\n\n";
    }
}
