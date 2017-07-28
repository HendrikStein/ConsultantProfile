package de.jastech.model;

import de.jastech.utils.DateUtils;
import lombok.Data;

import java.time.Period;
import java.util.Objects;

/**
 * @author Hendrik Stein
 */
@Data
public class AggregatedIndustryMapEntry {
    private Period period;
    private int projectAmount;
    private String industry;

    public AggregatedIndustryMapEntry(Period period, String industry) {
        this.period = period;
        this.projectAmount = 0;
        this.industry = industry;
    }

    public AggregatedIndustryMapEntry() {
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
     * Add a {@link Period}
     *
     * @param p2 {@link Period} to add
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
     * @param object {@link AggregatedIndustryMapEntry}
     * @return aggregated {@link AggregatedIndustryMapEntry}
     */
    public AggregatedIndustryMapEntry aggregate(AggregatedIndustryMapEntry object) {
        Objects.requireNonNull(object);
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
        return "AggregatedIndustryMapEntry{" +
                "period=" + period +
                ", projectAmount=" + projectAmount +
                ", industry='" + industry + '\'' +
                '}';
    }
}
