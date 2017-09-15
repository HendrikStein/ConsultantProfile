package de.jastech.model;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Data;

/**
 * Project model.
 * @autor Hendrik Stein
 */
@Data
public class Project {
  private String industry;
  private String name;
  private String customer;
  private String role;
  private int teamSize;
  private String ref;
  private Date start;
  private Date end;
  private String description;
  private List<Skill> techStack;
  private List<String> tasks;

  /**
   * Get end Date.
   * @return {@link Date}
   */
  public Date getEnd() {
    if (this.end == null) {
      this.end = new Date();
    }
    return this.end;
  }

  public Map<String, List<Skill>> aggregateSkillsByCategory() {
    return this.techStack.stream().collect(Collectors.groupingBy(Skill::getCat));
  }

}
