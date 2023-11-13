package com.tester;

import com.tester.gpt.Gpt;
import com.tester.gpt.Model;
import com.tester.junit.JUnitUtils;
import com.tester.prompt.PromptGenerator;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

public class DifferentialTester {

  public DifferentialTester() {}

  public void run() {
    Gpt gpt = new Gpt();
    String initialPrompt = PromptGenerator.getInitialPrompt("Label.java", "apply");
    gpt.generate(initialPrompt, Model.GPT4);
    String content = gpt.getLastMessage();
    String fileName = "LabelTest.java";
    JUnitUtils junitUtils = new JUnitUtils(fileName);
    junitUtils.extractTest(content);
    try {
      TestExecutionSummary workingSummary = junitUtils.runWorkingTest();
      TestExecutionSummary regressionSummary = junitUtils.runRegressionTest();
      long workingFailed = workingSummary.getTotalFailureCount();
      long regressionFailed = regressionSummary.getTotalFailureCount();
      System.out.println("Working failed: " + workingFailed);
      System.out.println("Regression failed: " + regressionFailed);
      boolean isSuccess = workingFailed == 0 && regressionFailed > 0;
      if (isSuccess) {
        System.out.println("Success");
      } else {
        System.out.println("Failed");
      }
    } catch(Exception e) {
      e.printStackTrace();
      System.out.println("Compile or run time error");
    }
  }
}
