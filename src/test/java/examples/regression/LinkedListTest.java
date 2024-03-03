package examples.regression;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedListTest {

    private LinkedList<String> list;

    @BeforeEach
    public void setUp() {
        list = new LinkedList<>();
    }

    // Subtask 3: Write tests for the working implementation of the get method
    @Test
    public void testGetElement() {
        // Add several elements
        for (int i=0; i<5; i++) {
            list.add("Test" + i);
        }

        // Assert that we get the correct element at position
        assertEquals("Test2", list.get(2));
    }

    @Test
    public void testGetFirstElement() {
        // Add one element
        list.add("First");

        // Assert that we can get it at position 0
        assertEquals("First", list.get(0));
    }

    @Test
    public void testGetElementWithInvalidPosition() {
        // Add several elements
        for (int i=0; i<5; i++) {
            list.add("Test" + i);
        }

        // Assert that IllegalArgumentException is thrown when trying to access element beyond the list size
        assertThrows(IllegalArgumentException.class, () -> {
            list.get(10);
        });
    }
}
