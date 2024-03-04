package com.tester.utils.executor;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Song Rui
 */
public class Executor {

  private final static String OS_WINDOWS = "windows";
  private final static String ERROR = "\n[ERROR] ";

  public static String exec(String cmd) {
    String os = System.getProperty("os.name").toLowerCase();
    ProcessBuilder pb = new ProcessBuilder();
    try {
      if (os.contains(OS_WINDOWS)) {
        pb.command("cmd.exe", "/c", cmd);
      } else {
        pb.command("bash", "-c", cmd);
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
