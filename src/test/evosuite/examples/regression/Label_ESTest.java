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

  // Newly added test cases
  @Test(timeout = 4000)
  public void labelInExcludes()  throws Throwable  {
    String[] includesArray = new String[] { "includedLabel1", "includedLabel2", "includedLabel3" };
    String[] excludesArray = new String[] { "excludedLabel" };
    Label label0 = new Label(includesArray, excludesArray);
    assertFalse(label0.apply("excludedLabel"));
  }

  @Test(timeout = 4000)
  public void labelNotInExcludesButInIncludes()  throws Throwable  {
    String[] includesArray = new String[] { "includedLabel" };
    String[] excludesArray = new String[] { "excludedLabel" };
    Label label0 = new Label(includesArray, excludesArray);
    assertTrue(label0.apply("includedLabel"));
  }

  @Test(timeout = 4000)
  public void labelNotInAnyArray()  throws Throwable  {
    String[] includesArray = new String[] { "includedLabel" };
    String[] excludesArray = new String[] { "excludedLabel" };
    Label label0 = new Label(includesArray, excludesArray);
    assertFalse(label0.apply("notInAnyArrayLabel"));
  }

  @Test(timeout = 4000)
  public void excludesIsNullButLabelInIncludes()  throws Throwable  {
    String[] includesArray = new String[] { "includedLabel" };
    Label label0 = new Label(includesArray, null);
    assertTrue(label0.apply("includedLabel"));
  }

  @Test(timeout = 4000)
  public void includesIsNull()  throws Throwable  {
    String[] excludesArray = new String[] { "excludedLabel" };
    Label label0 = new Label(null, excludesArray);
    assertTrue(label0.apply("notExcludedLabel"));
  }

  @Test(timeout = 4000)
  public void bothArraysAreNull()  throws Throwable  {
    Label label0 = new Label(null, null);
    assertTrue(label0.apply("anyLabel"));
  }
}
