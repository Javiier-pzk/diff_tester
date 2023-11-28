package examples.regression;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class LabelTest {

  @Test
  void applyTest() {
    // Prepare two Labels, each of which rejects one String and accepts another
    String rejected1 = "rejected1";
    String accepted1 = "accepted1";
    Label label1 = new Label(new String[] {accepted1}, new String[] {rejected1});

    String rejected2 = "rejected2";
    String accepted2 = "accepted2";
    Label label2 = new Label(new String[] {accepted2}, new String[] {rejected2});

    // Test for label1
    assertFalse(label1.apply(rejected1)); // rejected String should be removed
    assertTrue(label1.apply(accepted1)); // included String should pass

    // Test for label2
    assertFalse(label2.apply(rejected2)); // rejected String should be removed
    assertTrue(label2.apply(accepted2)); // included String should pass
  }

  @Test
  void applyTestWhenIncludeNull() {
     String rejected = "rejected";
     Label label = new Label(null, new String[] {rejected});
     assertFalse(label.apply(rejected)); // rejected String should be removed
     assertTrue(label.apply("sometext")); // This should pass as include is null
  }

  @Test
  void applyTestWhenExcludeNull() {
    String included = "included";
    Label label = new Label(new String[] {included}, null);
    assertTrue(label.apply(included)); // included String should pass
    assertFalse(label.apply("sometext")); // This should not pass as it's not included
  }
}
