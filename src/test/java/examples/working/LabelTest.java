package examples.working;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LabelTest {

  @Test
  void testApplyWhenExcluded() {
    String[] includes = {"includedLabel"};
    String[] excludes = {"excludedLabel"};
    Label label = new Label(includes, excludes);

    // This will fail on regressive version as it returns true for excluded label not found in 'includes' 
    assertFalse(label.apply("excludedLabel"));
  }
}
