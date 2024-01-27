package examples.working;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegressionExampleTest {

    private RegressionExample regressionExample;

    @BeforeEach
    public void setUp() {
        regressionExample = new RegressionExample();
    }

    @Test
    public void testExample123ForNegativeInput() {
        int result = regressionExample.example123(-100000002);
        assertEquals(-100000001, result);
    }

    @Test
    public void testExample123ForNonNegativeInput() {
        int result = regressionExample.example123(0);
        assertEquals(2, result);
    }

    @Test
    public void testExample123ForExtremePositiveInput() {
        int result = regressionExample.example123(Integer.MAX_VALUE);
        assertEquals(Integer.MIN_VALUE, result);
    }
    
    @Test
    public void testExample123ForBoundaryInput() {
        int result = regressionExample.example123(-100000001);
        assertEquals(-100000000, result);  // This assertion should fail for the regression version
    }
}
