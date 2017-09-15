package de.jastech.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Hendrik Stein
 */
public class WordUtilsTests {

    @Test
    public void testCalcTabsForProjectSummary() {
        String wordLower8 = "123";
        String wordEqual8 = "12345678";
        String wordLower16 = "1234567891234";
        String wordEqual16 = "1234567812345678";
        String wordLower24 = "123456781234567812345";
        String wordEqual24 = "123456781234567812345678";
        String wordLower32 = "12345678123456781234567812345";
        String wordEqual32 = "12345678123456781234567812345678";
        String wordHigher32 = "123456781234567812345678123456788937489734";


        Assertions.assertEquals(4, WordUtils.calcTabsForProjectSummary(wordLower8));
        Assertions.assertEquals(3, WordUtils.calcTabsForProjectSummary(wordEqual8));
        Assertions.assertEquals(3, WordUtils.calcTabsForProjectSummary(wordLower16));
        Assertions.assertEquals(2, WordUtils.calcTabsForProjectSummary(wordEqual16));
        Assertions.assertEquals(2, WordUtils.calcTabsForProjectSummary(wordLower24));
        Assertions.assertEquals(1, WordUtils.calcTabsForProjectSummary(wordEqual24));
        Assertions.assertEquals(1, WordUtils.calcTabsForProjectSummary(wordLower32));
        Assertions.assertEquals(0, WordUtils.calcTabsForProjectSummary(wordEqual32));
        Assertions.assertEquals(0, WordUtils.calcTabsForProjectSummary(wordHigher32));
    }

    @Test
    public void testCalcTabsForProjectSummaryWordIsNull() {
        try {
            WordUtils.calcTabsForProjectSummary(null);
            Assertions.fail("Exception expected");
        } catch (NullPointerException npe) {
        	Assertions.assertEquals("This value must not be null", npe.getMessage());
        }
    }
}
