package de.jastech.model;

import java.util.List;
import java.util.Objects;

import lombok.Data;

/**
 * Profile model.
 * @author Hendrik Stein
 */
@Data
public class Profile {
  private Person person;
  private List<Project> projects;

  private Profile() {

  }

  /**
   * Constructor.
   * 
   * @param person
   *          {@link Person}
   * @param projects
   *          List of {@link Project}
   * @return {@link Profile}
   */
  public static Profile of(Person person, List<Project> projects) {
    Profile profile = new Profile();
    Objects.requireNonNull(person);
    Objects.requireNonNull(projects);
    profile.setPerson(person);
    profile.setProjects(projects);
    return profile;
  }

}
