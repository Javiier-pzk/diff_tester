package examples.working;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LabelTest {
  
  @Test
  public void testApply() {
    String[] includes = { "a", "b", "c" };
    String[] excludes = { "d", "e", "f" };
    Label label = new Label(includes, excludes);
    assertFalse(label.apply("a"));
    assertFalse(label.apply("d"));
  }

  @Test
  public void testApplyWithNullIncluding() {
    String[] excludes = { "d", "e", "f" };
    Label label = new Label(null, excludes);
    assertFalse(label.apply("d"));
  }

  @Test
  public void testApplyWithNullExcluding() {
    String[] includes = { "a", "b", "c" };
    Label label = new Label(includes, null);
    assertTrue(label.apply("a"));
  }
}