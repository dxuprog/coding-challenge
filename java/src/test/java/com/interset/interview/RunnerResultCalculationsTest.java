package com.interset.interview;

import com.interset.interview.PopulationCalcUtil;
import com.interset.interview.PopulationEntryDto;
import com.interset.interview.PopulationResult;
import com.interset.interview.Runner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RunnerResultCalculationsTest {

    private Runner runner;
    private Set<PopulationEntryDto> population;
    PopulationResult populationResult;

    @Before
    public void setUp() throws Exception {
        this.runner = new Runner();
        population = runner.parsePopulation("src/test/resources/small_population_sample.csv");
        populationResult = PopulationCalcUtil.generateResult(population);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void siblingAverage() {
        // 13 / 6 = 2.16~
        // With rounding should be 3.
        Assert.assertEquals(3, PopulationCalcUtil.calculateAverageSiblings(populationResult));
    }

    @Test
    public void topFoods() {
        List<String> topFoods = PopulationCalcUtil.getTopFoods(populationResult);
        Assert.assertEquals(topFoods.get(0).toLowerCase(), "chicken");
        Assert.assertEquals((int) populationResult.getFavouriteFoods().get(topFoods.get(0).toLowerCase()), 3);

        Assert.assertEquals(topFoods.get(1).toLowerCase(), "peanut butter");
        Assert.assertEquals((int) populationResult.getFavouriteFoods().get(topFoods.get(1).toLowerCase()), 2);

        Assert.assertEquals(topFoods.get(2).toLowerCase(), "mozzarella cheese");
        Assert.assertEquals((int) populationResult.getFavouriteFoods().get(topFoods.get(2).toLowerCase()), 1);
    }

    @Test
    public void birthMonths() {
        // march, october, october, december, april, january
        Map<Month, Integer> birthMonthFrequency = populationResult.getBirthMonthFrequency();

        Assert.assertEquals((int) birthMonthFrequency.get(Month.MARCH), 1);
        Assert.assertEquals((int) birthMonthFrequency.get(Month.OCTOBER), 2);
        Assert.assertEquals((int) birthMonthFrequency.get(Month.DECEMBER), 1);
        Assert.assertEquals((int) birthMonthFrequency.get(Month.APRIL), 1);
        Assert.assertEquals((int) birthMonthFrequency.get(Month.JANUARY), 1);

        Assert.assertNull(birthMonthFrequency.get(Month.JUNE));
   }

    @Test
    public void testTimezoneConversion() {
        // The test sample for David has birth time: Friday, February 1, 1980 1:34:54 AM in GMT
        // But for his birth timezone of -5:00, it is: Thursday, January 31, 1980 8:34:54 PM GMT-05:00
        PopulationEntryDto david =
                population.stream().filter(
                (dto) -> dto.getLastName().equalsIgnoreCase("XU")).findFirst().orElse(null);

        LocalDateTime localDateTime = PopulationCalcUtil.getLocalDateTime(david);
        Assert.assertEquals(localDateTime.getMonth(), Month.JANUARY);

        // change his timezone to GMT to double check.
        david.setBirthTimezone("+00:00");
        localDateTime = PopulationCalcUtil.getLocalDateTime(david);
        Assert.assertEquals(localDateTime.getMonth(), Month.FEBRUARY);
    }
}