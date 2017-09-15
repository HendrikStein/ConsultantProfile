package de.jastech.service;

import de.jastech.collectors.AggregatedIndustryCollector;
import de.jastech.collectors.AggregatedSkillCollector;
import de.jastech.model.AggregatedIndustryMapEntry;
import de.jastech.model.AggregatedSkillMapEntry;
import de.jastech.model.Profile;
import de.jastech.model.Project;
import de.jastech.model.VitaEntry;
import de.jastech.utils.DateUtils;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Profile Service.
 * 
 * @author Hendrik Stein
 */
public class ProfileService {
  private Profile profile;

  public ProfileService(final Profile profile) {
    this.profile = profile;
  }

  /**
   * Get aggregated skills.
   *
   * @return Map of aggregated skills
   */
  private Map<String, AggregatedSkillMapEntry> getAggregatedSkillMap() {
    return this.profile.getProjects().stream().map(this::mapProjectsToAggregatedSkillMapEntry)
        .flatMap(List::stream).collect(Collectors.groupingBy(AggregatedSkillMapEntry::getEntry,
            new AggregatedSkillCollector()));
  }

  /**
   * Get aggregated industry experience.
   *
   * @return Map of aggregated industry experience
   */
  public Map<String, AggregatedIndustryMapEntry> getAggregatedIndustryMap() {

    Map<String, AggregatedIndustryMapEntry> result = this.profile.getProjects().stream()
        .map(this::mapProjectsToAggregatedIndustryMapEntry).collect(Collectors.groupingBy(
            AggregatedIndustryMapEntry::getIndustry, new AggregatedIndustryCollector()));

    Comparator<Map.Entry<String, AggregatedIndustryMapEntry>> comparator = (e1,
        e2) -> DateUtils.periodToDays(e2.getValue().getPeriod())
            - DateUtils.periodToDays(e1.getValue().getPeriod());

    Map<String, AggregatedIndustryMapEntry> sortedResult = result.entrySet().stream()
        .sorted(comparator).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (e1, e2) -> e1, LinkedHashMap::new));

    return sortedResult;
  }

  /**
   * Get aggregated skills for each category.
   *
   * @return Map of aggregated skill categories
   */
  public Map<String, List<AggregatedSkillMapEntry>> getAggregatedSkillMapByCategories() {
    Map<String, AggregatedSkillMapEntry> skillMap = this.getAggregatedSkillMap();

    Map<String, List<AggregatedSkillMapEntry>> result = skillMap.entrySet().stream()
        .map(Map.Entry::getValue).collect(Collectors.groupingBy(AggregatedSkillMapEntry::getCat));

    Comparator<AggregatedSkillMapEntry> comparator = (s1,
        s2) -> DateUtils.periodToDays(s1.getPeriod()) - DateUtils.periodToDays(s2.getPeriod());

    for (Map.Entry<String, List<AggregatedSkillMapEntry>> e : result.entrySet()) {
      e.getValue().sort(comparator.reversed());
    }
    return result;
  }

  /**
   * Sort Projects by Date.
   *
   * @param sortFunc
   *          Sort Function
   * @return sorted List
   */
  public List<Project> sortProjectsByDateDesc(Function<Project, Date> sortFunc) {
    if (sortFunc != null) {
      return this.profile.getProjects().stream().sorted(Comparator.comparing(sortFunc).reversed())
          .collect(Collectors.toList());
    }
    return this.profile.getProjects();
  }

  /**
   * Sort Vita by Date.
   *
   * @param sortFunc
   *          Sort Function
   * @return sorted List
   */
  public List<VitaEntry> sortVitaByDateDesc(Function<VitaEntry, Date> sortFunc) {
    if (sortFunc != null) {
      return this.profile.getPerson().getVita().stream()
          .sorted(Comparator.comparing(sortFunc).reversed()).collect(Collectors.toList());
    }
    return this.profile.getPerson().getVita();
  }

  /**
   * Create a list of {@link AggregatedSkillMapEntry}.
   *
   * @param project
   *          {@link Project}
   * @return {@link AggregatedSkillMapEntry}
   */
  private List<AggregatedSkillMapEntry> mapProjectsToAggregatedSkillMapEntry(Project project) {
    LocalDate startDate = project.getStart().toInstant().atZone(ZoneId.systemDefault())
        .toLocalDate();
    LocalDate endDate = LocalDate.now();
    if (project.getEnd() != null) {
      endDate = project.getEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    Period period = Period.between(startDate, endDate);

    return project.getTechStack().stream().map(s -> new AggregatedSkillMapEntry(s.getEntry(),
        s.getCat(), s.getVersions(), period, project.getIndustry())).collect(Collectors.toList());
  }

  /**
   * Create a {@link AggregatedIndustryMapEntry}.
   *
   * @param project
   *          {@link Project}
   * @return {@link AggregatedIndustryMapEntry}
   */
  private AggregatedIndustryMapEntry mapProjectsToAggregatedIndustryMapEntry(Project project) {
    LocalDate startDate = project.getStart().toInstant().atZone(ZoneId.systemDefault())
        .toLocalDate();
    LocalDate endDate = LocalDate.now();
    if (project.getEnd() != null) {
      endDate = project.getEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    Period period = Period.between(startDate, endDate);
    return new AggregatedIndustryMapEntry(period, project.getIndustry());
  }

}
