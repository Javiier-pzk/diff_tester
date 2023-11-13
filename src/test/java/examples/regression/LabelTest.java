package examples.regression;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LabelTest {

  @Test
  void applyTestWorking() {
    String[] includes = null;
    String[] excludes = {"label1", "label2"};
    Label label = new Label(includes, excludes);

    assertFalse(label.apply("label1"));
    assertTrue(label.apply("label3"));
  }

  @Test
  void applyTestBuggy() {
    String[] includes = null;
    String[] excludes = {"label1", "label2"};
    Label label = new Label(includes, excludes);

    assertTrue(label.apply("label1")); 
    assertTrue(label.apply("label3"));
  }
}

