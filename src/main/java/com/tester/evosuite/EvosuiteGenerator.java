package com.tester.evosuite;

import com.tester.conf.Conf;
import com.tester.utils.executor.Executor;

import java.io.File;

/**
 * @author Song Rui
 */
public class EvosuiteGenerator {

  private final String jarPath;
  private final String outputPath;
  private final Executor executor;

  public EvosuiteGenerator(String jarPath, String outputPath) {
    this.jarPath = jarPath.replace("\\", File.separator);
    this.outputPath = outputPath.replace("\\", File.separator);
    executor = new Executor();
    File evosuiteTestFileDir = new File(this.outputPath);
    if (!evosuiteTestFileDir.exists()) {
      evosuiteTestFileDir.mkdirs();
    }
  }

  public String generateWithCmd(String projectDir, String classFullName, String targetMethodName) {
    executor.setDirectory(new File(projectDir));
    executor.exec(Conf.MVN_COMPILE_TEST_CMD);

    executor.setDirectory(new File(this.outputPath));
    String cmdBuilder = Conf.JAR_CMD + this.jarPath +
            " -class " + classFullName +
            " -Dtarget_method " + targetMethodName +
            " -projectCP " + Conf.TARGET_CLASSES +
            " -criterion branch";
    System.out.println(cmdBuilder);
    return executor.exec(cmdBuilder);
  }
}
