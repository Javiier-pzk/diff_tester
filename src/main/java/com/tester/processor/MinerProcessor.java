package com.tester.processor;

import java.util.List;
import java.util.Map;

/**
 * @author Song Rui
 */
public class MinerProcessor extends BaseProcessor{

    /**
     * specified input and output info from RegMiner++
     */
    public static class MinerInfo {
        String baseDir;
        String workingCommit;
        String regressionCommit;
        String workingFilePath;
        String regressionFilePath;
        String outputPath;

        public MinerInfo(String baseDir, String workingCommit, String regressionCommit, String workingFilePath, String regressionFilePath, String outputPath) {
            this.baseDir = baseDir;
            this.workingCommit = workingCommit;
            this.regressionCommit = regressionCommit;
            this.workingFilePath = workingFilePath;
            this.regressionFilePath = regressionFilePath;
            this.outputPath = outputPath;
        }
    }

    MinerInfo minerInfo;

    public MinerProcessor(String programFileName, String testFileName, String targetMethod, Map<String, List<Integer>> suspiciousLines, MinerInfo minerInfo) {
        super(programFileName, testFileName, targetMethod, suspiciousLines);
        this.minerInfo = minerInfo;
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
