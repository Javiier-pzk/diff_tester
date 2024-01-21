package examples.regression;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LabelTest {

    @Test
    void bothNull() {
        Label label = new Label(null, null);
        assertTrue(label.apply("label"));
    }

    @Test
    void includesNonNullExcludesNull() {
        Label label = new Label(new String[]{"A", "B"}, null);
        assertTrue(label.apply("A"));
        assertFalse(label.apply("C"));
    }

    @Test
    void excludesNonNullIncludesNull() {
        Label label = new Label(null, new String[]{"X", "Y"});
        assertTrue(label.apply("A"));
        assertFalse(label.apply("X"));
    }

    @Test
    void bothNonNull() {
        Label label = new Label(new String[]{"A", "B"}, new String[]{"X", "Y"});
        assertTrue(label.apply("A"));
        assertFalse(label.apply("X"));
        assertFalse(label.apply("Z"));
    }
}
