package examples.regression;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LabelTest {

  @Test
  void testApply() {
    String[] includes = {"label1", "label2"};
    String[] excludes = {"label3", "label4"};

    Label label = new Label(includes, excludes);

    assertTrue(label.apply("label1"), "Should return true for included label.");
    assertFalse(label.apply("label4"), "Should return false for excluded label.");
    assertFalse(label.apply("label5"), "Should return false for labels not included.");

    excludes = null;
    Label labelWithNullExcludes = new Label(includes, excludes);
    assertTrue(labelWithNullExcludes.apply("label1"), "Should return true for included label with null excludes. ");
    assertFalse(labelWithNullExcludes.apply("label4"), "Should return false for previously excluded label when excludes is null.");

    includes = null;
    Label labelWithNullIncludes = new Label(includes, excludes);
    assertTrue(labelWithNullIncludes.apply("label3"), "Should return true for any label with null includes and excludes.");
  }
}
