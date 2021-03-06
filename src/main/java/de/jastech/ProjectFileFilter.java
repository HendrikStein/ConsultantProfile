package de.jastech;

import java.io.File;
import java.io.FileFilter;

/**
 * File filter.
 * 
 * @author Hendrik Stein
 */
public class ProjectFileFilter implements FileFilter {
  public boolean accept(File file) {
    return file.getName().contains("project");
  }
}
