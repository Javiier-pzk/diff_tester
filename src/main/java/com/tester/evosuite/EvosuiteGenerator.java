package com.tester.evosuite;

import com.tester.utils.executor.Executor;

import java.io.File;

/**
 * @author Song Rui
 */
public class EvosuiteGenerator {

  private static final String CLASS_OPTION = " -class ";
  private static final String TARGET_METHOD_OPTION = " -Dtarget_method \"";
  private static final String PROJECT_CP_OPTION = " -projectCP ";
  private static final String CRITERION_OPTION = " -criterion ";
  private static final String EVOSUITE_JAR_PATH = "libs/evosuite-shaded-1.0.7-SNAPSHOT.jar";
  private static final String TARGET_CLASSES = "target/classes";
  private static final String JAR_CMD = "java -jar ";
  private static final String MVN_COMPILE_TEST_CMD = "mvn compile test-compile";
  private static final String BRANCH = "branch";

  public static String generateWithCmd(String classFullName, String targetMethodName) {
    Executor.exec(MVN_COMPILE_TEST_CMD);
    String targetClasses = TARGET_CLASSES.replace("/", File.separator);
    String evosuiteJarPath = EVOSUITE_JAR_PATH.replace("/", File.separator);
    StringBuilder cmdBuilder = new StringBuilder();
    cmdBuilder.append(JAR_CMD).append(evosuiteJarPath)
            .append(CLASS_OPTION).append(classFullName)
            .append(TARGET_METHOD_OPTION).append(targetMethodName).append("\"")
            .append(PROJECT_CP_OPTION).append(targetClasses)
            .append(CRITERION_OPTION).append(BRANCH);
    System.out.println(cmdBuilder);
    return Executor.exec(cmdBuilder.toString());
  }
}
