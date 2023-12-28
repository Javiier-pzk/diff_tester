package examples.regression;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class LabelTest {

    private String[] includes;
    private String[] excludes;

    @BeforeEach
    void setUp() {
        includes = new String[]{"a", "b", "c"};
        excludes = new String[]{"x", "y", "z"};
    }

    @Test
    public void testApplyExclusion() {
        Label l = new Label(includes, excludes);
        assertFalse(l.apply("x"));
    }

    @Test
    public void testApplyInclusion() {
        Label l = new Label(includes, excludes);
        assertTrue(l.apply("a"));
    }

    @Test
    public void testApplyNotIncluded() {
        Label l = new Label(includes, excludes);
        assertFalse(l.apply("d"));
    }

    @Test
    public void testIncludeNullExcludeHasValue() {
        Label l = new Label(null, excludes);
        assertFalse(l.apply("x"));
        assertTrue(l.apply("a"));
    }

    @Test
    public void testIncludeHasValueExcludeNull() {
        Label l = new Label(includes, null);
        assertTrue(l.apply("a"));
        assertFalse(l.apply("x"));
    }
}
