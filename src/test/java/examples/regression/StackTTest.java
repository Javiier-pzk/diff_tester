package examples.regression;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class StackTTest {
    private StackT<Integer> stack;
    
    @BeforeEach
    public void setup() {
        stack = new StackT<>();
    }
    
    @AfterEach
    public void teardown() {
        stack = null;
    }

    @Test
    public void testPop() {
        stack.push(1);
        Integer val = stack.pop();
        assertEquals(1, val);
    }

    @Test
    public void testOrderOfRetrieval() {
        stack.push(5);
        stack.push(10);
        stack.push(20);
  
        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());
        assertEquals(5, stack.pop());
    }

    @Test
    public void testPopOnEmptyStack() {
        assertThrows(IllegalArgumentException.class, () -> stack.pop());
    }

    @Test
    public void testContinuity() {
        stack.push(10);
        assertEquals(10, stack.pop());
    
        stack.push(20);
        assertEquals(20, stack.pop());

        stack.push(15);
        assertEquals(15, stack.pop());
    }

    @Test
    public void testBoundary() {
        stack.push(10);
        stack.push(20);
        stack.pop(); 
        assertEquals(10, stack.pop());
    }
}
