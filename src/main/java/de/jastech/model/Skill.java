package de.jastech.model;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import lombok.Data;

/**
 * Skill model.
 * 
 * @author Hendrik Stein
 */
@Data
public class Skill {
  private String entry;
  private String cat;
  private Set<String> versions;

  @Override
  public String toString() {
    return "Skill{" + "entry='" + entry + '\'' + ", cat='" + cat + '\'' + ", versions=" + versions
        + '}';
  }

  /**
   * Constructor.
   * 
   * @param entry
   *          entry
   * @param cat
   *          category
   * @param versions
   *          set of versions
   */
  public Skill(String entry, String cat, Set<String> versions) {
    this.entry = entry;
    this.cat = cat;
    this.versions = versions;
  }

  public Skill() {
    // Empty constructor
  }

  private String toStringByEntryAndVersions() {
    if (Objects.nonNull(this.versions) && !this.versions.isEmpty()) {
      return this.entry + " (" + String.join(", ", versions) + ")";
    }
    return this.entry;
  }

  /**
   * Join Skill Strings.
   *
   * @param delimiter
   *          String delimiter
   * @param skills
   *          Iterable of {@link Skill}
   * @return Joined String of skills
   */
  public static String joinToString(String delimiter, Iterable<Skill> skills) {
    Iterator<Skill> iterator = skills.iterator();
    StringBuilder sb = new StringBuilder();
    while (iterator.hasNext()) {
      sb.append(iterator.next().toStringByEntryAndVersions());
      if (iterator.hasNext()) {
        sb.append(delimiter);
      }
    }
    return sb.toString();
  }
}
