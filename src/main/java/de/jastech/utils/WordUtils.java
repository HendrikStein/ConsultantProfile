package de.jastech.utils;

import java.util.Objects;

/**
 * Word Utilities.
 *
 * @author Hendrik Stein
 */
public class WordUtils {
  private WordUtils() {
    // Utility class
  }

  /**
   * Calculate tabs for project summary.
   *
   * @param word
   *          {@link String} Word
   * @return the number of tabs
   */
  public static final int calcTabsForProjectSummary(String word) {
    final int maxTabs = 4;
    final int charsForTab = 8;
    return calcTabs(word, maxTabs, charsForTab);
  }

  /**
   * Calculate tabs for a given word and a tab maximum.
   *
   * @param word
   *          {@link String} Word
   * @param maxTabs
   *          {@link Integer} Maximum amount of tabs
   * @param charsForTab
   *          {@link Integer} Chars for a Tab
   * @return calculated amount of tabs
   */
  private static final int calcTabs(String word, final int maxTabs, final int charsForTab) {
    Objects.requireNonNull(word, "This value must not be null");
    final int wordAmountTabs = word.length() / charsForTab;
    if (wordAmountTabs > maxTabs) {
      return 0;
    }
    return maxTabs - (word.length() / charsForTab);
  }

}
