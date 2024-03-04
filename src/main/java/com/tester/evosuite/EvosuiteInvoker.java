package com.tester.evosuite;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Song Rui
 */
public class EvosuiteInvoker {

  private final static String OS_WINDOWS = "windows";
  private final static String ERROR = "\n[ERROR] ";
  private static final String MVN_COMPILE_TEST_CMD = "mvn compile test-compile";
  private static final String JAVA_VERSION = "java.version";
  private static final String JAVA_8 = "1.8";
  private static final String OS_NAME = "os.name";
  private static final String CMD_EXE = "cmd.exe";
  private static final String BASH = "bash";
  private static final String WINDOWS_COMMAND_OPTION = "/c";
  private static final String UNIX_COMMAND_OPTION = "-c";

  public static String execute(String cmd) {
    executeCommand(MVN_COMPILE_TEST_CMD);
    return executeCommand(cmd);
  }

  private static String executeCommand(String cmd) {
    if (!System.getProperty(JAVA_VERSION).startsWith(JAVA_8)) {
      return ERROR + "Java 8 is required to run Evosuite. Please check your Java version";
    }
    String os = System.getProperty(OS_NAME).toLowerCase();
    ProcessBuilder pb = new ProcessBuilder();
    try {
      if (os.contains(OS_WINDOWS)) {
        pb.command(CMD_EXE, WINDOWS_COMMAND_OPTION,cmd);
      } else {
        pb.command(BASH, UNIX_COMMAND_OPTION, cmd);
      }
      Process process = pb.start();
      InputStreamReader inputStr = new InputStreamReader(process.getInputStream());
      InputStreamReader errorStr = new InputStreamReader(process.getErrorStream());
      BufferedReader inputReader = new BufferedReader(inputStr);
      BufferedReader errorReader = new BufferedReader(errorStr);
      StringBuilder builder = new StringBuilder();
      String line;
      while ((line = inputReader.readLine()) != null) {
        builder.append("\n").append(line);
      }
      while ((line = errorReader.readLine()) != null) {
        builder.append(ERROR).append(line);
      }
      process.destroy();
      IOUtils.close(inputStr);
      IOUtils.close(inputReader);
      return builder.toString();
    } catch (IOException ex) {
      ex.printStackTrace();
      return "";
    }
  }
}
