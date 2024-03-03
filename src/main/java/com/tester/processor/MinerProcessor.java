package com.tester.processor;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
        byte[] codeBytes = code.getBytes();
        byte[] packageNameBytes = minerInfo.packageName.getBytes();
        try {
            if (Files.exists(Paths.get(minerInfo.outputPath))) {
                Files.delete(Paths.get(minerInfo.outputPath));
            }
            FileChannel channel = FileChannel.open(Paths.get(minerInfo.outputPath), StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            channel.write(ByteBuffer.wrap(packageNameBytes));
            channel.write(ByteBuffer.wrap(codeBytes));
            channel.force(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    String getFilePath(String dir, String type, String fileName) {
        if (WORKING.equals(type)) {
            return minerInfo.workingFilePath;
        } else {
            return minerInfo.regressionFilePath;
        }
    }

    @Override
    public String extractFailures(List<MavenTestFailure> failures) {
        StringBuilder sb = new StringBuilder();
        for (MavenTestFailure failure : failures) {
            sb.append("Method: ");
            sb.append(failure.getMethodName());
            sb.append("\n");
            int lineNum = failure.getFailureLineNumber();
            String filePath = minerInfo.outputPath;
            String programLine = getProgramLine(lineNum, filePath);
            sb.append("Line ");
            sb.append(lineNum);
            sb.append(": ");
            sb.append(programLine);
            sb.append("\n");
            List<String> stackTrace = failure.getStackTrace();
            int limit = Math.min(stackTrace.size(), 10);
            sb.append("Exception:\n");
            for (int i = 0; i < limit; i++) {
                sb.append(stackTrace.get(i));
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    String getClassPath(String type, String fileName) {
        String baseName;
        if (WORKING.equals(type)) {
            baseName = minerInfo.workingFilePath;
        } else {
            baseName = minerInfo.regressionFilePath;
        }
        return getBaseName(baseName).replace(SRC + MAIN, TARGET_DIR) + CLASS;
    }

    @Override
    String getQualifiedClassName(String type) {
        return getBaseName(testFileName);
    }

    @Override
    String getPackageName(String type) {
        return PACKAGE + minerInfo.packageName + ";\n\n";
    }

}
