package examples.working;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LabelTest {
    private Label labelWithIncludes;
    private Label labelWithExcludes;

    @BeforeEach
    void setUp() {
        String[] includes = {"include1", "include2"};
        String[] excludes = {"exclude1", "exclude2"};
        labelWithIncludes = new Label(includes, null);
        labelWithExcludes = new Label(null, excludes);
    }

    @Test
    void testLabelNotInIncludes() {
        assertFalse(labelWithIncludes.apply("not-in-includes"));
    }

    @Test
    void testLabelNotInExcludes() {
        assertTrue(labelWithExcludes.apply("not-in-excludes"));
    }
}
