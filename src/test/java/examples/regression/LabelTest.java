package examples.regression;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LabelTest {

    @Test
    void testApply() {
        String[] includes = {"apple", "banana"};
        String[] excludes = {"grapefruit"};

        examples.working.Label workingLabel = new examples.working.Label(includes, excludes);

        // These tests should pass with the working code
        assertTrue(workingLabel.apply("apple"));
        assertFalse(workingLabel.apply("grapefruit"));
        assertFalse(workingLabel.apply("orange"));
    }

    @Test
    void testApplyWithRegressedCode() {
        String[] includes = {"apple", "banana"};
        String[] excludes = {"grapefruit"};

        examples.regression.Label regressedLabel = new examples.regression.Label(includes, excludes);
        examples.working.Label workingLabel = new examples.working.Label(includes, excludes);

        // This should pass, as "apple" is included in both versions
        assertTrue(regressedLabel.apply("apple"));

        // This should fail with the regression version as "grapefruit" is excluded, but regression version will return true
        assertFalse(regressedLabel.apply("grapefruit"));

        // This test should pass, it's expecting false for non-included "orange" in both versions
        assertFalse(regressedLabel.apply("orange")); 
    }
}
