package com.tester.evosuite;

import com.tester.conf.Conf;
import com.tester.utils.executor.Executor;

import java.io.File;

/**
 * @author Song Rui
 */
public class EvosuiteGenerator {

    private String jarPath;
    private String outputPath;
    private Executor executor;

    public EvosuiteGenerator(String jarPath, String outputPath){
        this.jarPath = jarPath.replace("\\", File.separator);
        this.executor = new Executor();
        this.outputPath = outputPath.replace("\\", File.separator);
        File evosuiteTestFileDir = new File(this.outputPath);
        if (!evosuiteTestFileDir.exists()) {
            evosuiteTestFileDir.mkdirs();
        }
    }

    public String generateWithCmd(String projectDir, String classFullName, String targetMethodName) {
        executor.setDirectory(new File(projectDir));
        executor.exec(Conf.MVN_COMPILE_TEST_CMD);

        this.executor.setDirectory(new File(this.outputPath));
        String cmdBuilder = Conf.JAR_CMD + this.jarPath +
                " -class " + classFullName +
                " -Dtarget_method " + targetMethodName +
                " -projectCP " + projectDir + File.separator + Conf.TARGET_CLASSES;

        return executor.exec(cmdBuilder);
    }
}
