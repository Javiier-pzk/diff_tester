package com.tester.evosuite;

import com.tester.conf.Conf;
import com.tester.utils.executor.Executor;

import java.io.File;

/**
 * @author Song Rui
 */
public class EvosuiteGenerator {

    private String jarPath;
    private Executor executor;

    public EvosuiteGenerator(String jarPath, String outputPath){
        this.jarPath = jarPath.replace("\\", File.separator);
        this.executor = new Executor();
        File evosuiteTestFileDir = new File(outputPath.replace("\\", File.separator));
        if (!evosuiteTestFileDir.exists()) {
            evosuiteTestFileDir.mkdirs();
        }
        this.executor.setDirectory(evosuiteTestFileDir);
    }

    public String generateWithCmd(String projectDir, String classFullName, String targetMethodName) {
        executor.setDirectory(new File(projectDir));
        executor.exec(Conf.MVN_COMPILE_TEST_CMD);
        String cmdBuilder = Conf.JAR_CMD + this.jarPath +
                " -class " + classFullName +
                " -Dtarget_method " + targetMethodName +
                " -projectCP " + projectDir + File.separator + Conf.TARGET_CLASSES;
        return this.executor.exec(cmdBuilder);
    }
}
