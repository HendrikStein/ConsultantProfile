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
     * @param word {@link String} Word
     * @return the number of tabs
     */
    public final static int calcTabsForProjectSummary(String word) {
        final int MAX_TABS = 4;
        final int CHARS_FOR_TAB = 8;
        return calcTabs(word, MAX_TABS, CHARS_FOR_TAB);
    }

    /**
     * Calculate Tabs for Skill matrix.
     *
     * @param word    {@link String} Word
     * @param maxTabs Maximum amount of tabs
     * @return {@link Integer} calculated amount of tabs
     */
    public final static int calcTabsForSkillMatrix(String word, int maxTabs) {
        final int CHARS_FOR_TAB = 8;
        return calcTabs(word, maxTabs, CHARS_FOR_TAB);
    }

    /**
     * Calculate tabs for a given word and a tab maximum.
     *
     * @param word        {@link String} Word
     * @param maxTabs     {@link Integer} Maximum amount of tabs
     * @param charsForTab {@link Integer} Chars for a Tab
     * @return calculated amount of tabs
     */
    private final static int calcTabs(String word, final int maxTabs, final int charsForTab) {
        Objects.requireNonNull(word, "This value must not be null");
        final int wordAmountTabs = word.length() / charsForTab;
        if (wordAmountTabs > maxTabs) {
            return 0;
        }
        return maxTabs - (word.length() / charsForTab);
    }


}
