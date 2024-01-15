package com.tester;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.tester.gpt.Gpt;
import com.tester.gpt.Model;
import com.tester.processor.*;
import com.tester.prompt.PromptGenerator;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class DifferentialTester {

  private final String testFileName;
  private final String targetMethod;
  private final Gpt gpt;
  private final Logger logger;
  private final BaseProcessor baseProcessor;

  public DifferentialTester(String programFileName,
                            String testFileName,
                            String targetMethod,
                            Map<String, List<Integer>> suspiciousLines) {
    this.testFileName = testFileName;
    this.targetMethod = targetMethod;
    gpt = new Gpt();
    baseProcessor = new TestProcessor(programFileName, testFileName, targetMethod, suspiciousLines);
    logger = Logger.getLogger(DifferentialTester.class.getName());
    logger.setLevel(Level.INFO);
  }

  public DifferentialTester(String programFileName,
                            String testFileName,
                            Map<String, List<Integer>> suspiciousLines) {
    this.testFileName = testFileName;
    this.targetMethod = "";
    gpt = new Gpt();
    baseProcessor = new TestProcessor(programFileName, testFileName, suspiciousLines);
    logger = Logger.getLogger(DifferentialTester.class.getName());
    logger.setLevel(Level.INFO);
  }

  public DifferentialTester(String programFileName,
                            String testFileName,
                            String targetMethod,
                            Map<String, List<Integer>> suspiciousLines, MinerProcessor.MinerInfo minerInfo) {
    this.targetMethod = targetMethod;
    this.testFileName = testFileName;
    gpt = new Gpt();
    baseProcessor = new MinerProcessor(programFileName, testFileName, targetMethod, suspiciousLines, minerInfo);
    logger = Logger.getLogger(DifferentialTester.class.getName());
    logger.setLevel(Level.INFO);
  }

  public void run() {
    String workingProgram = baseProcessor.readWorkingProgram();
    String regressionProgram = baseProcessor.readRegressionProgram();
    String prompt = PromptGenerator.getInitialPrompt(
            workingProgram, regressionProgram, targetMethod);
    while (true) {
      logger.info("Prompt:\n" + prompt + "\n");
      gpt.generate(prompt, Model.GPT4);
      String content = gpt.getLastMessage();
      logger.info("Gpt response:\n" + content + "\n");
      baseProcessor.extractTest(content);
      try {
        MavenTestExecutionSummary workingSummary = baseProcessor.runWorkingTest();
        MavenTestExecutionSummary regressionSummary = baseProcessor.runRegressionTest();
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
        List<MavenTestFailure> failures = workingSummary.getFailures();
        String failuresString = baseProcessor.extractFailures(failures);
        if (workingPassed == 0 && workingFailed == 0) {
          prompt = PromptGenerator.getCompileErrorPrompt(failuresString);
        } else if (workingFailed > 0) {
          prompt = PromptGenerator.getTestsFailedInWorkingPrompt(workingFailed, failuresString);
        } else if (regressionFailed == 0) {
          String coverageString = baseProcessor.extractWorkingCoverageInfo(workingSummary);
          prompt = PromptGenerator.getNoTestsFailedInRegressionPrompt(coverageString);
        }
      } catch (MavenInvocationException e) {
        logger.info("Failed to compile test suite. Re-prompting...\n");
        String exception = baseProcessor.extractException(e);
        prompt = PromptGenerator.getCompileErrorPrompt(exception);
      }
    }
  }
}
