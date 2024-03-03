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
        String packageName;
        String workingContent;
        String regressionContent;

        public MinerInfo(String baseDir, String workingCommit, String regressionCommit, String workingFilePath, String regressionFilePath, String outputPath, String packageName, String workingContent, String regressionContent) {
            this.baseDir = baseDir;
            this.workingCommit = workingCommit;
            this.regressionCommit = regressionCommit;
            this.workingFilePath = workingFilePath;
            this.regressionFilePath = regressionFilePath;
            this.outputPath = outputPath;
            this.packageName = packageName;
            this.regressionContent = regressionContent;
            this.workingContent = workingContent;
        }
    }

    MinerInfo minerInfo;

    public MinerProcessor(String programFileName, String testFileName, String targetMethod, Map<String, List<Integer>> suspiciousLines, MinerInfo minerInfo) {
        super(programFileName, testFileName, targetMethod, suspiciousLines);
        this.minerInfo = minerInfo;
    }

    @Override
    protected void writeTestToFile(String code) {
        writeToFile(minerInfo.packageName, code, minerInfo.outputPath);
    }

    @Override
    public String readWorkingProgram() {
        return this.minerInfo.workingContent;
//        return readProgram(getFilePath(MAIN, WORKING, ""));
    }

    @Override
    public String readRegressionProgram() {
        return this.minerInfo.regressionContent;
//        return readProgram(getFilePath(BaseProcessor.getMAIN(), BaseProcessor.getREGRESSION(), ""));
    }

    @Override
    protected String getFilePath(String dir, String type, String fileName) {
        if (WORKING.equals(type)) {
            return minerInfo.workingFilePath;
        } else {
            return minerInfo.regressionFilePath;
        }
    }

    @Override
    public String extractFailures(List<MavenTestFailure> failures) {
        return extractFailures(failures, minerInfo.outputPath);
    }

    @Override
    protected String getClassPath(String type, String fileName) {
        String baseName;
        if (WORKING.equals(type)) {
            baseName = minerInfo.workingFilePath;
        } else {
            baseName = minerInfo.regressionFilePath;
        }
        return getBaseName(baseName).replace(SRC + MAIN, TARGET_DIR) + CLASS;
    }

    @Override
    protected String getQualifiedClassName(String type) {
        return getBaseName(testFileName);
    }

    @Override
    protected String getPackageName(String type) {
        return PACKAGE + minerInfo.packageName + ";\n\n";
    }
}
