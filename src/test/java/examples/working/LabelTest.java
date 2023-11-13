package examples.working;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LabelTest {

    @Test
    public void testApply() {
        String[] includes = {"Label1", "Label2", "Label3"};
        String[] excludes = {"Label4", "Label5", "Label6"};
        
        Label label = new Label(includes, excludes);

        // "Label2" is present in "includes" array.
        assertTrue(label.apply("Label2"));

        // "Label5" is present in "excludes" array.
        assertFalse(label.apply("Label5"));
    }
}
