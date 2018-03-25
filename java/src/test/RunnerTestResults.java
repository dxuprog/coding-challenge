import com.interset.interview.PopulationCalcUtil;
import com.interset.interview.PopulationEntryDto;
import com.interset.interview.PopulationResult;
import com.interset.interview.Runner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Month;
import java.util.Map;
import java.util.Set;

public class RunnerTestResults {

    private Runner runner;
    private Set<PopulationEntryDto> population;
    PopulationResult populationResult;

    @Before
    public void setUp() throws Exception {
        this.runner = new Runner();

    }

    @After
    public void tearDown() {
    }

    @Test
    public void birthMonthCountMatchPopulation() throws Exception {
        population = runner.parsePopulation("src/test/resources/population_sample.json");
        populationResult = PopulationCalcUtil.generateResult(population);

        int populationCount = population.size();

        int birthCount = 0;
        Map<Month, Integer> birthMonthFrequency = populationResult.getBirthMonthFrequency();
        for (Map.Entry<Month, Integer> birthFreq : birthMonthFrequency.entrySet()) {
            birthCount += birthFreq.getValue();
        }

        Assert.assertEquals(populationCount, birthCount);
    }

    @Test
    public void ignoreDuplicates() throws Exception {
        population = runner.parsePopulation("src/test/resources/small_population_dupes.csv");
        populationResult = PopulationCalcUtil.generateResult(population);

        // there are 9 entries, but only 6 unique ones
        Assert.assertEquals(population.size(), 6);
    }

    @Test
    public void basicPrintResults() throws Exception{
        // check for exceptions thrown
        population = runner.parsePopulation("src/test/resources/population_sample.csv");
        populationResult = PopulationCalcUtil.generateResult(population);
        PopulationCalcUtil.printResults(populationResult);
    }
}