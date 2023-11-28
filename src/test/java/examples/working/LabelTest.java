package examples.working;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LabelTest {

    @Test
    void applyTestWithIncludesOnly() {
        String[] includes = {"abc", "xyz"};
        String[] excludes = null;
        Label label = new Label(includes, excludes);
        assertTrue(label.apply("abc"));
        assertFalse(label.apply("pqr"));
    }

    @Test
    void applyTestWithExcludesOnly() {
        String[] includes = null;
        String[] excludes = {"abc", "xyz"};
        Label label = new Label(includes, excludes);
        assertTrue(label.apply("pqr"));
        assertFalse(label.apply("abc"));
    }

    @Test
    void applyTestWithBothIncludesAndExcludes() {
        String[] includes = {"abc", "xyz"};
        String[] excludes = {"pqr", "def"};
        Label label = new Label(includes, excludes);
        assertTrue(label.apply("abc"));
        assertFalse(label.apply("pqr"));
    }
}
