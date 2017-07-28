package de.jastech.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Hendrik Stein
 */
public class DateUtilsTests {

    @Test
    public void testEmptyPeriod() {
        Period period = Period.of(0, 0, 0);
        Assertions.assertEquals("", DateUtils.format(period));
    }

    @Test
    public void testPeriodOneYear() {
        Period period = Period.of(1, 0, 0);
        Assertions.assertEquals("1 Jahr", DateUtils.format(period));
    }

    @Test
    public void testPeriodMoreYears() {
        Period period = Period.of(3, 0, 0);
        Assertions.assertEquals("3 Jahre", DateUtils.format(period));
    }

    @Test
    public void testPeriodOneMonth() {
        Period period = Period.of(0, 1, 0);
        Assertions.assertEquals("1 Monat", DateUtils.format(period));
    }

    @Test
    public void testPeriodMultipleMonth() {
        Period period = Period.of(0, 22, 0);
        Assertions.assertEquals("22 Monate", DateUtils.format(period));
    }

    @Test
    public void testPeriodMultipleYearsAndMonths() {
        Period period = Period.of(5, 10, 0);
        Assertions.assertEquals("5 Jahre 10 Monate", DateUtils.format(period));
    }

    @Test
    public void testPeriodOneYearAndMonths() {
        Period period = Period.of(1, 10, 0);
        Assertions.assertEquals("1 Jahr 10 Monate", DateUtils.format(period));
    }

    @Test
    public void testPeriodOneYearAndOneMonth() {
        Period period = Period.of(1, 1, 0);
        Assertions.assertEquals("1 Jahr 1 Monat", DateUtils.format(period));
    }

    @Test
    public void testPeriodMultipleYearsAndMultipleMonths() {
        Period period = Period.of(5, 5, 0);
        Assertions.assertEquals("5 Jahre 5 Monate", DateUtils.format(period));
    }

    @Test
    public void testPeriodToDaysWithYear() {
        Period period = Period.of(1, 0, 0);
        Assertions.assertEquals(360, DateUtils.periodToDays(period));
    }

    @Test
    public void testPeriodToDaysWithYearAndMonths() {
        Period period = Period.of(1, 2, 0);
        Assertions.assertEquals(420, DateUtils.periodToDays(period));
    }

    @Test
    public void testPeriodToDaysWithYearAndMonthsAndDays() {
        Period period = Period.of(1, 2, 2);
        Assertions.assertEquals(422, DateUtils.periodToDays(period));
    }

    @Test
    public void addPeriodTest() {
        Period p1 = Period.of(1, 5, 0);
        Period p2 = Period.of(1, 11, 0);

        Period p = DateUtils.addPeriods(p1, p2);
        Assertions.assertEquals("3 Jahre 4 Monate", DateUtils.format(p));

        p1 = Period.of(1, 1, 0);
        p2 = Period.of(1, 11, 0);

        p = DateUtils.addPeriods(p1, p2);
        Assertions.assertEquals("3 Jahre", DateUtils.format(p));
    }

    @Test
    public void calcPeriodTest() throws ParseException {
        String start = "2017-03-01";
        String end = "2017-07-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(start);
        Date endDate = sdf.parse(end);

        Period period = DateUtils.calculatePeriod(startDate, endDate);
        Assertions.assertEquals(Period.of(0,4,0), period);
    }
}
