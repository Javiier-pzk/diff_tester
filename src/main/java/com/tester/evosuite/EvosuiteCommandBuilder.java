package com.tester.evosuite;

import java.io.File;

/**
 * @author Song Rui
 */
public class EvosuiteCommandBuilder {

  private static final String CLASS_OPTION = " -class ";
  private static final String TARGET_METHOD_OPTION = " -Dtarget_method \"";
  private static final String PROJECT_CP_OPTION = " -projectCP ";
  private static final String CRITERION_OPTION = " -criterion ";
  private static final String EVOSUITE_JAR_PATH = "libs/evosuite-shaded-1.0.7-SNAPSHOT.jar";
  private static final String TARGET_CLASSES = "target/classes";
  private static final String JAR_CMD = "java -jar ";
  private String command;

  public EvosuiteCommandBuilder() {
    command = JAR_CMD + EVOSUITE_JAR_PATH.replace("/", File.separator);
  }

  public EvosuiteCommandBuilder withDefaultProjectCP() {
    command += PROJECT_CP_OPTION + TARGET_CLASSES.replace("/", File.separator);
    return this;
  }

  public  EvosuiteCommandBuilder withProjectCP(String projectCP) {
    command += PROJECT_CP_OPTION + projectCP.replace("/", File.separator);
    return this;
  }

  public EvosuiteCommandBuilder withClass(String fullyQualifiedClassName) {
    command += CLASS_OPTION + fullyQualifiedClassName;
    return this;
  }

  public EvosuiteCommandBuilder withTargetMethod(String targetMethod) {
    command += TARGET_METHOD_OPTION + targetMethod + "\"";
    return this;
  }

  public EvosuiteCommandBuilder withCriterion(String criterion) {
    command += CRITERION_OPTION + criterion;
    return this;
  }

  public String build() {
    return command;
  }
}
