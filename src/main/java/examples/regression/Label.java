package examples.regression;

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
      return Arrays.binarySearch(excludes, label) == -1;
    }

    return includes != null && Arrays.binarySearch(includes, label) >= 0;
  }
}
