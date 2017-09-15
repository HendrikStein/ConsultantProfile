package de.jastech.service;

import de.jastech.input.InputReader;
import de.jastech.model.AggregatedIndustryMapEntry;
import de.jastech.model.AggregatedSkillMapEntry;
import de.jastech.model.Profile;
import de.jastech.model.Project;

import java.io.FileNotFoundException;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/**
 * Profile service tests.
 * 
 * @author Hendrik Stein
 */
public class ProfileServiceTest {

  private static ProfileService service;

  /**
   * Setup tests.
   * 
   * @throws FileNotFoundException
   *           if file not found
   */
  @BeforeAll
  public static void setUp() throws FileNotFoundException {
    System.out.println("Before all");
    Profile profile = InputReader.readProfile("src//test//resources");
    service = new ProfileService(profile);
  }

  @Test
  public void testGetAggregatedIndustryMap() {
    Map<String, AggregatedIndustryMapEntry> result = service.getAggregatedIndustryMap();
    Set<String> industries = getIndustry();
    Assertions.assertEquals(industries, result.keySet());

    AggregatedIndustryMapEntry entry = result.get("Bank");
    Assertions.assertNotNull(entry);
    Period period = Period.of(2, 0, 0);
    Assertions.assertEquals(period, entry.getPeriod());

    entry = result.get("Versicherung");
    Assertions.assertNotNull(entry);
    period = Period.of(0, 6, 0);
    Assertions.assertEquals(period, entry.getPeriod());

    entry = result.get("Fun");
    Assertions.assertNotNull(entry);
    period = Period.of(0, 1, 0);
    Assertions.assertEquals(period, entry.getPeriod());
  }

  @Test
  public void testGetAggregatedSkillMapByCategories() {
    System.out.println("testGetAggregatedSkillMapByCategories");
    Set<String> categories = getCategories();
    Map<String, List<AggregatedSkillMapEntry>> result = service.getAggregatedSkillMapByCategories();
    Assertions.assertEquals(categories, result.keySet());

    this.testInfrastructure(result.get("Infrastruktur"));
    this.testLanguages(result.get("Programmiersprache"));
  }

  private void testLanguages(List<AggregatedSkillMapEntry> aggregatedSkills) {
    Assertions.assertEquals(2, aggregatedSkills.size());

    AggregatedSkillMapEntry entry = aggregatedSkills.get(0);
    Assertions.assertEquals(3, entry.getProjectAmount());
    Period period = Period.of(2, 7, 0);
    Assertions.assertEquals(period, entry.getPeriod());

    entry = aggregatedSkills.get(1);
    Assertions.assertEquals(2, entry.getProjectAmount());
    period = Period.of(2, 6, 0);
    Assertions.assertEquals(period, entry.getPeriod());
  }

  private void testInfrastructure(List<AggregatedSkillMapEntry> aggregatedSkills) {
    Assertions.assertEquals(1, aggregatedSkills.size());
    AggregatedSkillMapEntry entry = aggregatedSkills.get(0);
    Assertions.assertEquals(1, entry.getProjectAmount());
    Period period = Period.of(2, 0, 0);
    Assertions.assertEquals(period, entry.getPeriod());
  }

  @Test
  public void testSortProjectsByDateDesc() {
    List<Project> result = service.sortProjectsByDateDesc(Project::getEnd);
    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals("Scoreboard", result.get(0).getName());
    Assertions.assertEquals("Osplus Portal Neo", result.get(1).getName());
    Assertions.assertEquals("Brillenversicherung", result.get(2).getName());
  }

  private Set<String> getCategories() {
    Set<String> cat = new HashSet<>();
    cat.add("Infrastruktur");
    cat.add("Programmiersprache");
    cat.add("Framework");
    cat.add("SCM");
    return cat;
  }

  private Set<String> getIndustry() {
    Set<String> cat = new HashSet<>();
    cat.add("Bank");
    cat.add("Fun");
    cat.add("Versicherung");
    return cat;
  }

}
