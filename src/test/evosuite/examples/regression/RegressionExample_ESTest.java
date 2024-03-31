package examples.regression;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class RegressionExample_ESTest extends RegressionExample_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test0()  throws Throwable  {
        RegressionExample regressionExample0 = new RegressionExample();
        int int0 = regressionExample0.example123(928);
        assertEquals(930, int0);
    }

    @Test(timeout = 4000)
    public void test1()  throws Throwable  {
        RegressionExample regressionExample0 = new RegressionExample();
        int int0 = regressionExample0.example123((-100000056));
        assertEquals((-100000055), int0);
    }
}
