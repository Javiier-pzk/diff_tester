package examples.regression;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class Label_ESTest extends Label_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      String[] stringArray0 = new String[5];
      stringArray0[0] = "gGrW+";
      stringArray0[1] = "examples.working.Label";
      stringArray0[2] = "";
      stringArray0[3] = "";
      stringArray0[4] = "";
      String[] stringArray1 = new String[0];
      Label label0 = new Label(stringArray0, stringArray1);
      boolean boolean0 = label0.apply("examples.working.Label");
      assertTrue(boolean0);
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      String[] stringArray0 = new String[1];
      stringArray0[0] = "7+ TEa";
      Label label0 = new Label(stringArray0, stringArray0);
      boolean boolean0 = label0.apply("7+ TEa");
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      String[] stringArray0 = new String[2];
      stringArray0[0] = "";
      stringArray0[1] = "0(bJ-)@p'P";
      Label label0 = new Label(stringArray0, stringArray0);
      boolean boolean0 = label0.apply("c");
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test3()  throws Throwable  {
      Label label0 = new Label((String[]) null, (String[]) null);
      boolean boolean0 = label0.apply("g;e6hFKdge^<%-8z2)e");
      assertTrue(boolean0);
  }

  @Test(timeout = 4000)
  public void test4() throws Throwable  {
      String[] stringArray0 = new String[1];
      stringArray0[0] = "testLabel";
      String[] stringArray1 = new String[1];
      Label label0 = new Label(stringArray0, stringArray1);
      boolean boolean0 = label0.apply("testLabel");
      assertFalse(boolean0);
  }
}