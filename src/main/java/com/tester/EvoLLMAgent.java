package com.tester;

import com.tester.gpt.*;
import com.tester.processor.*;
import com.tester.prompt.*;
import org.apache.maven.shared.invoker.*;

import java.util.*;
import java.util.logging.*;

public class EvoLLMAgent {

  private final Gpt gpt;
  private final Logger logger;
  private final String targetMethod;
  private final String fileName;
  private final EvosuiteProcessor evosuiteProcessor;

  public EvoLLMAgent(String fileName,
                     String targetMethod,
                     Map<String, List<Integer>> suspiciousLines) {
    this.fileName = fileName;
    this.targetMethod = targetMethod;
    gpt = new Gpt();
    evosuiteProcessor = new EvosuiteProcessor(fileName, targetMethod, suspiciousLines);
    logger = Logger.getLogger(EvoLLMAgent.class.getName());
    logger.setLevel(Level.INFO);
  }

  public EvoLLMAgent(String fileName, Map<String, List<Integer>> suspiciousLines) {
    this.fileName = fileName;
    this.targetMethod = "";
    gpt = new Gpt();
    evosuiteProcessor = new EvosuiteProcessor(fileName, suspiciousLines);
    logger = Logger.getLogger(EvoLLMAgent.class.getName());
    logger.setLevel(Level.INFO);
  }

  public void run() {
//    evosuiteProcessor.generateTest();
//    evosuiteProcessor.extractTest();
    try {
      MavenTestExecutionSummary workingSummary = evosuiteProcessor.runWorkingTest();
      MavenTestExecutionSummary regressionSummary = evosuiteProcessor.runRegressionTest();
      if (workingSummary.getTestsFailedCount() > 0) {
        throw new IllegalStateException("Working version has failing Evosuite tests");
      }
      logger.info("All tests passed on working version");
      if (regressionSummary.getTestsFailedCount() > 0) {
        logger.info("Regression bug is detectable from Evosuite generated tests");
        return;
      }
      logger.info("Regression is not detectable from Evosuite generated tests");
      modifyTestsWithLLM(workingSummary);
    } catch (MavenInvocationException e) {
      e.printStackTrace();
    }
  }

  private void modifyTestsWithLLM(MavenTestExecutionSummary generatedWorkingSummary) {
    String testSuite =  evosuiteProcessor.readTestSuite();
    String workingSuspiciousLines = evosuiteProcessor.readWorkingSuspiciousLines();
    String regressionSuspiciousLines = evosuiteProcessor.readRegressionSuspiciousLines();
    String coverage = evosuiteProcessor.extractWorkingCoverageInfo(generatedWorkingSummary);
    String prompt = PromptGenerator.getEvoLLMAgentPrompt(
            testSuite, workingSuspiciousLines, regressionSuspiciousLines, coverage);
    logger.info("Prompt: " + prompt);
    gpt.generate(prompt, Model.GPT4);
    String content = gpt.getLastMessage();
    runSubtask(content);
    runImplementation();
  }

  private void runSubtask(String response) {
    List<String> subtasks = evosuiteProcessor.extractSubtasks(response);
    for (String subtask : subtasks) {
      logger.info("\n\nSubtask: " + subtask + "\n");
      String prompt = PromptGenerator.getSubtaskPrompt(subtask);
      gpt.generate(prompt, Model.GPT4);
      boolean isDecomposable = gpt.getLastMessage().contains("Yes");
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

  public void runImplementation() {
    String testFileName = evosuiteProcessor.getTestFileName();
    String prompt = PromptGenerator.getEvoFullImplementationPrompt(
            evosuiteProcessor.getBaseName(testFileName));
    while (true) {
      gpt.generate(prompt, Model.GPT4);
      String implementation = gpt.getLastMessage();
      evosuiteProcessor.extractTest(implementation);
      logger.info("Implementation: " + implementation);
      String fileExtension = evosuiteProcessor.getExtension(testFileName);
      if (!fileExtension.equals(".java")) {
        logger.info("Successfully generated test. " +
                "View the generated test in " + testFileName);
        break;
      }
      try {
        MavenTestExecutionSummary workingSummary = evosuiteProcessor.runWorkingTest();
        MavenTestExecutionSummary regressionSummary = evosuiteProcessor.runRegressionTest();
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
                  "View the generated test in " + fileName);
          break;
        }
        logger.info("Failed to detect regression bug. Re-prompting...\n");
        List<MavenTestFailure> failures = workingSummary.getFailures();
        String failuresString = evosuiteProcessor.extractFailures(failures);
        if (workingFailed == -1 || regressionFailed == -1) {
          prompt = PromptGenerator.getCompileErrorPrompt(failuresString);
        } else if (workingFailed > 0) {
          prompt = PromptGenerator.getEvoTestsFailedInWorkingPrompt(workingFailed, failuresString);
        } else if (regressionFailed == 0) {
          String coverageString = evosuiteProcessor.extractWorkingCoverageInfo(workingSummary);
          prompt = PromptGenerator.getEvoNoTestsFailedInRegressionPrompt(coverageString);
        }
      } catch (MavenInvocationException e) {
        e.printStackTrace();
      }
    }
  }
}
