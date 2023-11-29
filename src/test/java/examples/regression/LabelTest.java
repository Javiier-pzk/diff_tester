package examples.regression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class LabelTest {

  @Test
  public void testLabelExcludesPresent() {
    Label labelObj = new Label(null, new String[]{"testLabel"});
    boolean result = labelObj.apply("testLabel");
    assertEquals(false, result);
  }

  @Test
  public void testLabelExcludesAbsentAndIncludePresent() {
    Label labelObj = new Label(new String[]{"testLabel"}, null);
    boolean result = labelObj.apply("testLabel");
    assertEquals(true, result);
  }

  @Test
  public void testLabelExcludesAbsentAndIncludeAbsent() {
    Label labelObj = new Label(null, null);
    boolean result = labelObj.apply("testLabel");
    assertEquals(true, result);
  }

  @Test
  public void testLabelIncludesPresentAndExcludesAbsent() {
    Label labelObj = new Label(new String[]{"testLabel"}, new String[]{});
    boolean result = labelObj.apply("testLabel");
    assertEquals(true, result);
  }

  @Test
  public void testLabelExcludesPresentAndIncludePresent() {
    Label labelObj = new Label(new String[]{"testLabel"}, new String[]{"testLabel"});
    boolean result = labelObj.apply("testLabel");
    assertEquals(false, result);
  }
}
