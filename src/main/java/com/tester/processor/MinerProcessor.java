package com.tester.processor;

import java.util.List;
import java.util.Map;

public class MinerProcessor extends BaseProcessor{

    public MinerProcessor(String programFileName, String testFileName, String targetMethod, Map<String, List<Integer>> suspiciousLines, String baseDir) {
        super(programFileName, testFileName, targetMethod, suspiciousLines);
        super.setBaseDir(baseDir);
    }

    @Override
    protected void writeTest2File(String code) {

    }

    @Override
    String getFilePath(String dir, String type, String fileName) {
        return null;
    }

    @Override
    String getClassPath(String type, String fileName) {
        return null;
    }

    @Override
    String getQualifiedClassName(String type) {
        return null;
    }

    @Override
    String getPackageName(String type) {
        return null;
    }
}
