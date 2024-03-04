## Motivation and context
Regression testing is a critical part of software development. 
It is the process of testing a software application to ensure that new code changes have not adversely affected the existing functionality. 
Regression bugs can easily occur when the test suite of a software application is not comprehensive enough.
If not caught early, regression bugs can lead to serious issues such as data corruption, security vulnerabilities, and even system crashes.
However, this is easier said than done since regression bugs are often difficult to spot and regression testing is a time-consuming and labor-intensive process.
Hence, this project aims to provide an automated solution to detect regression bugs early by using a technique called differential testing.

## Differential Testing
Differential testing is a software testing technique that compares the output of two or more software implementations to ensure that they are functionally equivalent.
Here is the high level overview of the differential testing process:
1. **Input Generation**: Generate a set of test inputs (test suite) for the software implementations to be tested.
2. **Execution**: Execute the test suite on the software implementations and record their outputs.
3. **Comparison**: Compare the outputs of the software implementations to detect any discrepancies.
4. **Error Reporting**: Report any discrepancies as potential regression bugs.
5. **Debugging**: Investigate the discrepancies to determine the root cause of the regression bugs.
6. **Fixing**: Fix the regression bugs and re-run the test suite to ensure that the bugs have been resolved.

## diff_tester
`diff_tester` is a hybrid LLM-based differential testing tool that is designed to detect regression bugs in Java.
It uses a combination of [OpenAI's GPT-4](https://platform.openai.com/docs/guides/text-generation/chat-completions-api) large language model and [Evosuite](https://github.com/EvoSuite/evosuite) to generate test inputs for the software implementations to be tested.

## Building and running the project
1. Since this project is a [Maven](https://maven.apache.org/install.html) project, ensure you have it installed on your machine.
2. Clone the repository and navigate to the root directory of the project.
3. Run the following command to build the project:
```bash
mvn clean install
```
4. You need to provide a `.env` file in the root directory of the project with the following environment variables: `OPENAI_API_KEY` and `MVN_PATH`. 
There is an `example.env` file in the root directory of the project that you can use as a template.
5. You can invoke the `diff_tester` tool by following the examples in these files in the `examples` directory:
   - [DifferentialTesterTest.java](https://github.com/Javiier-pzk/diff_tester/blob/main/src/test/java/com/tester/DifferentialTesterTest.java): Invoke `diff_tester` to carry out differential testing.
   - [LLMAgentTest.java](https://github.com/Javiier-pzk/diff_tester/blob/main/src/test/java/com/tester/LLMAgentTest.java): Invoke the `diff_tester` LLM Agent to carry out differential testing.
6. Since we are also using the JaCoCo Maven plugin to generate test coverage reports, you have to set `-javaagent` option before running the tests.
```bash
-javaagent:<PATH_TO_MAVEN_JACOCO_JAR>=destfile=target/jacoco.exec,output=file
```
Replace `<PATH_TO_MAVEN_JACOCO_JAR>` with the path to the JaCoCo Maven plugin JAR file. It can usually be found at `~/.m2/repository/org/jacoco/org.jacoco.agent/0.8.11/org.jacoco.agent-0.8.11-runtime.jar`.





