package com.tester;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DifferentialTesterTest {

  @Test
  void runTest() {
    DifferentialTester dt = new DifferentialTester();
    dt.run();
  }

  @Test
  void runJunitTests() {
  }
}