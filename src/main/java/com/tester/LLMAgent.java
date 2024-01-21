package com.tester;

import com.tester.gpt.*;
import com.tester.processor.*;
import com.tester.prompt.*;
import org.apache.maven.shared.invoker.*;

import java.util.*;
import java.util.logging.*;

public class LLMAgent {

  private final String testFileName;
  private final String targetMethod;
  private final Gpt gpt;
  private final Logger logger;
  private final TestProcessor testProcessor;

  public LLMAgent(String programFileName,
                  String testFileName,
                  String targetMethod,
                  Map<String, List<Integer>> suspiciousLines) {
    this.testFileName = testFileName;
    this.targetMethod = targetMethod;
    gpt = new Gpt();
    testProcessor = new TestProcessor(programFileName, testFileName, targetMethod, suspiciousLines);
    logger = Logger.getLogger(DifferentialTester.class.getName());
    logger.setLevel(Level.INFO);
  }

  public LLMAgent(String programFileName,
                  String testFileName,
                  Map<String, List<Integer>> suspiciousLines) {
    this.testFileName = testFileName;
    this.targetMethod = "";
    gpt = new Gpt();
    testProcessor = new TestProcessor(programFileName, testFileName, suspiciousLines);
    logger = Logger.getLogger(DifferentialTester.class.getName());
    logger.setLevel(Level.INFO);
  }

  public void run() {
    String workingProgram = testProcessor.readWorkingProgram();
    String regressionProgram = testProcessor.readRegressionProgram();
    String prompt = PromptGenerator.getLLMAgentPrompt(
            workingProgram, regressionProgram, targetMethod);
    gpt.generate(prompt, Model.GPT4);
    String content = gpt.getLastMessage();
    runSubtask(content);
    runImplementation();
  }

  private void runSubtask(String response) {
    List<String> subtasks = testProcessor.extractSubtasks(response);
    for (String subtask : subtasks) {
      logger.info("\n\nSubtask: " + subtask);
      String prompt = PromptGenerator.getSubtaskPrompt(subtask);
      gpt.generate(prompt, Model.GPT4);
      boolean isDecomposable = gpt.getLastMessage().equals("Yes");
      if (isDecomposable) {
        logger.info("decomposable");
        prompt = PromptGenerator.getFurtherDecompositionNeededPrompt(subtask);
        gpt.generate(prompt, Model.GPT4);
        runSubtask(gpt.getLastMessage());
        continue;
      }
      logger.info("not decomposable");
      prompt = PromptGenerator.getNoFurtherDecompositionPrompt(subtask);
      gpt.generate(prompt, Model.GPT4);
      boolean isImplementable = gpt.getLastMessage().equals("Yes");
      if (!isImplementable) {
        logger.info("not implementable");
        continue;
      }
      logger.info("implementable");
      prompt = PromptGenerator.getImplementablePrompt(subtask);
      gpt.generate(prompt, Model.GPT4);
      String implementation = gpt.getLastMessage();
      logger.info("Implementation: " + implementation);
    }
  }

  private void runImplementation() {
    String prompt = PromptGenerator.getFullImplementationPrompt(
            testProcessor.getBaseName(testFileName));
    logger.info("\n\nPrompt: " + prompt);
    while (true) {
      gpt.generate(prompt, Model.GPT4);
      String implementation = gpt.getLastMessage();
      testProcessor.extractTest(implementation);
      logger.info("Implementation: " + implementation);
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
        List<MavenTestFailure> failures = workingSummary.getFailures();
        String failuresString = testProcessor.extractFailures(failures);
        if (workingPassed == 0 && workingFailed == 0) {
          prompt = PromptGenerator.getCompileErrorPrompt(failuresString);
        } else if (workingFailed > 0) {
          prompt = PromptGenerator.getTestsFailedInWorkingPrompt(workingFailed, failuresString);
        } else if (regressionFailed == 0) {
          String coverageString = testProcessor.extractWorkingCoverageInfo(workingSummary);
          prompt = PromptGenerator.getNoTestsFailedInRegressionPrompt(coverageString);
        }
      } catch (MavenInvocationException e) {
        logger.info("Failed to compile test suite. Re-prompting...\n");
        String exception = testProcessor.extractException(e);
        prompt = PromptGenerator.getCompileErrorPrompt(exception);
      }
    }
  }
}
