package examples.regression;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegressionExampleTest {
    
    @Test
    void testExample123() {
        RegressionExample example = new RegressionExample();
         
        int a = -100000001;
        assertEquals(a + 1, example.example123(a));
         
        int b = -100000000;
        assertEquals(b + 2, example.example123(b));
         
        int c = 0;
        assertEquals(c + 2, example.example123(c));
    }
}
