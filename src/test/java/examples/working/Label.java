package examples.working;

import java.util.Arrays;

public class Label {

  private String[] includes;
  private String[] excludes;

  public Label(String[] includes, String[] excludes) {
    if (includes != null) {
      this.includes = new String[includes.length];
      System.arraycopy(includes, 0, this.includes, 0, includes.length);
      Arrays.sort(this.includes);
    }

    if (excludes != null) {
      this.excludes = new String[excludes.length];
      System.arraycopy(excludes, 0, this.excludes, 0, excludes.length);
      Arrays.sort(this.excludes);
    }
  }

  public boolean apply(String label) {
    if (excludes != null) {
      if (Arrays.binarySearch(excludes, label) >= 0) {
        return false;
      }
    }
    if (includes != null) {
      if (Arrays.binarySearch(includes, label) >= 0) {
        return true;
      }

      return false;
    }
    return true;
  }
}