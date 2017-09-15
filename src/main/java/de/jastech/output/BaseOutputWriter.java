package de.jastech.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Basic output writer.
 * 
 * @author Hendrik Stein
 */
public class BaseOutputWriter {
  protected StringBuilder output;

  public BaseOutputWriter() {
    this.output = new StringBuilder();
  }

  /**
   * Write file.
   * 
   * @param fileName
   *          Filename
   */
  public void writeFile(String fileName) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
      bw.append(this.output);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the {@link ResourceBundle}.
   *
   * @param locale
   *          {@link Locale}
   * @return the {@link ResourceBundle}
   */
  protected final ResourceBundle getResourceBundle(Locale locale) {
    return ResourceBundle.getBundle("Profile", locale);
  }
}
