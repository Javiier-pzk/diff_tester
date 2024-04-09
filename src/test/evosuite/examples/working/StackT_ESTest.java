package examples.working;

/*
 * This file was automatically generated by EvoSuite
 * Tue Apr 09 10:32:12 GMT 2024
 */


import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class StackT_ESTest extends StackT_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      StackT<Integer> stackT0 = new StackT<Integer>();
      // Undeclared exception!
      try { 
        stackT0.pop();
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Stack empty
         //
         verifyException("examples.working.StackT", e);
      }
  }
}