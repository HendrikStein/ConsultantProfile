package de.jastech.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * @author Hendrik Stein
 */
public class DateUtils {
    private static SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");

    private DateUtils() {
        // Utils
    }

    /**
     * Format a date.
     *
     * @param date the {@link Date} to format
     * @return the formatted date
     */
    public static String format(Date date) {
        if (Objects.isNull(date)|| isToday(date)) {
            return "heute";
        }
        return formatter.format(date);
    }

    /**
     * Checks if the given date is today.
     *
     * @param date {@link Date}
     * @return <b>true</b> if same date as today (dd/MM/yyyy) <b>else</b> false
     */
    public static boolean isToday(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormatter.format(date).equals(dateFormatter.format(new Date()));
    }

    /**
     * Format a period. Only months and years will be concerned.
     *
     * @param period the {@link Period} to format
     * @return the formatted period
     */
    public static String format(Period period) {
        StringBuilder builder = new StringBuilder();
        if (period.getYears() > 0) {
            builder.append(period.getYears());
            builder.append(period.getYears() > 1 ? " Jahre" : " Jahr");
        }

        if (period.getMonths() > 0) {
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(period.getMonths());
            builder.append(period.getMonths() > 1 ? " Monate" : " Monat");
        }

        return builder.toString();
    }

    /**
     * Get approx days of {@link Period}
     *
     * @param period the period
     * @return period in days
     */
    public static int periodToDays(Period period) {
        Objects.requireNonNull(period);
        return (period.getYears() * 12 + period.getMonths()) * 30 + period.getDays();
    }

    /**
     * Add Periods. Only months will be aggregated to years.
     *
     * @param p1 period 1
     * @param p2 period 2
     * @return the aggregated period
     */
    public static Period addPeriods(Period p1, Period p2) {
        int years = p1.getYears() + p2.getYears();
        int months = p1.getMonths() + p2.getMonths();

        if (months >= 12) {
            years += months / 12;
            months = months % 12;
        }
        return Period.of(years, months, 0);
    }

    /**
     * Calculate the period for a start and end date.
     *
     * @param start {@link Date}
     * @param end   {@link Date}
     * @return {@link Period}
     */
    public static Period calculatePeriod(Date start, Date end) {
        LocalDate startDate = LocalDate.now();
        if (start != null) {
            startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        LocalDate endDate = LocalDate.now();
        if (end != null) {
            endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return Period.between(startDate, endDate);
    }
}
