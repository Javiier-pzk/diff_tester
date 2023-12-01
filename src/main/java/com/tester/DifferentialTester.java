package com.tester;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.tester.gpt.Gpt;
import com.tester.gpt.Model;
import com.tester.processor.TestProcessor;
import com.tester.processor.MavenTestExecutionSummary;
import com.tester.processor.MavenTestFailure;
import com.tester.prompt.PromptGenerator;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class DifferentialTester {

  private final String testFileName;
  private final String targetMethod;
  private final Gpt gpt;
  private final Logger logger;
  private final TestProcessor testProcessor;

  public DifferentialTester(String programFileName, String testFileName, String targetMethod) {
    this.testFileName = testFileName;
    this.targetMethod = targetMethod;
    gpt = new Gpt();
    testProcessor = new TestProcessor(programFileName, testFileName, targetMethod);
    logger = Logger.getLogger(DifferentialTester.class.getName());
    logger.setLevel(Level.INFO);
  }

  public DifferentialTester(String programFileName, String testFileName) {
    this.testFileName = testFileName;
    this.targetMethod = "";
    gpt = new Gpt();
    testProcessor = new TestProcessor(programFileName, testFileName);
    logger = Logger.getLogger(DifferentialTester.class.getName());
    logger.setLevel(Level.INFO);
  }

  public void run() {
    String workingProgram = testProcessor.readWorkingProgram();
    String regressionProgram = testProcessor.readRegressionProgram();
    String prompt = PromptGenerator.getInitialPrompt(
            workingProgram, regressionProgram, targetMethod);
    while (true) {
      logger.info("Prompt:\n" + prompt + "\n");
      gpt.generate(prompt, Model.GPT4);
      String content = gpt.getLastMessage();
      logger.info("Gpt response:\n" + content + "\n");
      testProcessor.extractTest(content);
      try {
        MavenTestExecutionSummary workingSummary = testProcessor.runWorkingTest();
        MavenTestExecutionSummary regressionSummary = testProcessor.runRegressionTest();
        long workingPassed = workingSummary.getTestsSucceededCount();
        long workingFailed = workingSummary.getTestsFailedCount();
        long regressionPassed = regressionSummary.getTestsSucceededCount();
        long regressionFailed = regressionSummary.getTestsFailedCount();
        logger.info(
                "Tests passed on working version: " + workingPassed + "\n" +
                "Tests passed on regression version: " + regressionPassed + "\n" +
                "Tests failed on working version: " + workingFailed + "\n" +
                "Tests failed on regression version: " + regressionFailed + "\n");
        boolean isSuccess = workingFailed == 0 && regressionFailed > 0;
        if (isSuccess) {
          logger.info("Successfully detected regression bug. " +
                  "View the generated test in " + testFileName);
          break;
        }
        logger.info("Failed to detect regression bug. Re-prompting...\n");
        if (regressionFailed == 0) {
          String workingCoverage = testProcessor.extractWorkingTestCoverageInfo();
          prompt = PromptGenerator.getNoTestsFailedInRegressionPrompt(workingCoverage);
          continue;
        }
        List<MavenTestFailure> failures = workingSummary.getFailures();
        String failuresString = testProcessor.extractFailures(failures);
        prompt = PromptGenerator.getTestsFailedInWorkingPrompt(workingFailed, failuresString);
      } catch (MavenInvocationException e) {
        logger.info("Failed to compile test suite. Re-prompting...\n");
        String exception = testProcessor.extractException(e);
        prompt = PromptGenerator.getCompileErrorPrompt(exception);
      }
    }
  }
}
