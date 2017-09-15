package de.jastech.model;

import de.jastech.utils.DateUtils;
import java.time.Period;
import java.util.Objects;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Aggregated skill entry.
 * 
 * @author Hendrik Stein
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AggregatedSkillMapEntry extends Skill {
  private Period period;
  private int projectAmount;
  private String industry;

  /**
   * Constructor.
   * 
   * @param entry
   *          entry
   * @param cat
   *          category
   * @param versions
   *          versions
   * @param period
   *          {@link Period}
   * @param industry
   *          industry
   */
  public AggregatedSkillMapEntry(String entry, String cat, Set<String> versions, Period period,
      String industry) {
    super(entry, cat, versions);
    this.period = period;
    this.projectAmount = 0;
    this.industry = industry;
  }

  public AggregatedSkillMapEntry() {
    // Empty constructor
  }

  public Period getPeriod() {
    return period;
  }

  public String getIndustry() {
    return industry;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }

  /**
   * Add a {@link Period}.
   *
   * @param p2
   *          {@link Period} to add
   */
  public void addPeriod(Period p2) {
    this.period = DateUtils.addPeriods(this.period, p2);
  }

  /**
   * Increment the project count.
   */
  public void incProjectAmount() {
    this.projectAmount++;
  }

  /**
   * Aggregate skill data.
   *
   * @param object
   *          {@link AggregatedSkillMapEntry}
   * @return aggregated {@link AggregatedSkillMapEntry}
   */
  public AggregatedSkillMapEntry aggregate(AggregatedSkillMapEntry object) {
    Objects.requireNonNull(object);
    if (this.getCat() == null) {
      this.setCat(object.getCat());
    }

    if (this.getVersions() == null) {
      this.setVersions(object.getVersions());
    } else {
      this.getVersions().addAll(object.getVersions());
    }

    if (this.getEntry() == null) {
      this.setEntry(object.getEntry());
    }

    if (this.getIndustry() == null) {
      this.setIndustry(object.getIndustry());
    }

    if (this.getPeriod() == null) {
      this.period = object.getPeriod();
    } else {
      this.addPeriod(object.getPeriod());
    }

    this.incProjectAmount();
    return this;
  }

  @Override
  public String toString() {
    return "AggregatedSkillMapEntry{" + "period=Y:" + period.getYears() + " M:" + period.getMonths()
        + " D:" + period.getDays() + ", projectAmount=" + projectAmount + '}' + super.toString();
  }
}
