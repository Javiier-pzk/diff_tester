package com.tester.processor;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class EvosuiteProcessorTest {

  @Test
  void extractTest() {
    EvosuiteProcessor evosuiteProcessor = new EvosuiteProcessor(
            "Label.java", "apply", null);
    evosuiteProcessor.extractTest();
  }
}