package examples.regression;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Random;

public class LinkedListTest {

    //...
    @Test
    public void testGetOnRandomLargeIndexes() {
        LinkedList<String> list = new LinkedList<>();
        Random random = new Random();
        int NUMBER_OF_ELEMENTS = 1000;
        for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
            list.add("Element " + i);
        }
        int randomIndex = random.nextInt(NUMBER_OF_ELEMENTS);
        assertEquals("Element " + randomIndex, list.get(randomIndex));
    }
}
