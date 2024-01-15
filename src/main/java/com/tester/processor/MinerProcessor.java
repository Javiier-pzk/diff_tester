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

        public MinerInfo(String baseDir, String workingCommit, String regressionCommit, String workingFilePath, String regressionFilePath, String outputPath, String packageName) {
            this.baseDir = baseDir;
            this.workingCommit = workingCommit;
            this.regressionCommit = regressionCommit;
            this.workingFilePath = workingFilePath;
            this.regressionFilePath = regressionFilePath;
            this.outputPath = outputPath;
            this.packageName = packageName;
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
        if (BaseProcessor.getWORKING().equals(type)) {
            return minerInfo.workingFilePath;
        } else {
            return minerInfo.regressionFilePath;
        }
    }

    @Override
    String getClassPath(String type, String fileName) {
        String baseName;
        if (BaseProcessor.getWORKING().equals(type)) {
            baseName = minerInfo.workingFilePath;
        } else {
            baseName = minerInfo.regressionFilePath;
        }
        return getBaseName(baseName).replace(BaseProcessor.getSRC()+BaseProcessor.getMAIN(), BaseProcessor.getTargetDir()) + BaseProcessor.getCLASS();
    }

    @Override
    String getQualifiedClassName(String type) {
        return getBaseName(super.getTestFileName());
    }

    @Override
    String getPackageName(String type) {
        return BaseProcessor.getPACKAGE() + minerInfo.packageName + ";\n\n";
    }

}
