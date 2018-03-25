import com.interset.interview.Runner;
import com.interset.interview.PopulationEntryDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Set;

public class RunnerTestIO {

    private Runner runner;

    @Before
    public void setUp() throws Exception {
        this.runner = new Runner();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testEmptyList() throws Exception {
        Set<PopulationEntryDto> population = runner.parsePopulation("src/test/resources/empty_sample.csv");
        Assert.assertTrue(population.isEmpty());
    }

    @Test
    public void readJsonAndPrint() throws Exception{
        Set<PopulationEntryDto> population = runner.parsePopulation("src/test/resources/population_sample.json");
        printTen(population);
        Assert.assertFalse(population.isEmpty());
    }

    @Test
    public void readCsvAndPrint() throws Exception {
        Set<PopulationEntryDto> population = runner.parsePopulation("src/test/resources/population_sample.csv");
        printTen(population);
        Assert.assertFalse(population.isEmpty());
    }

    @Test
    public void readGzipJsonAndPrint() throws Exception{
        Set<PopulationEntryDto> population = runner.parsePopulation("src/test/resources/population_sample.json.gz");
        printTen(population);
        Assert.assertFalse(population.isEmpty());
    }

    @Test
    public void readGzipCsvAndPrint() throws Exception {
        Set<PopulationEntryDto> population = runner.parsePopulation("src/test/resources/population_sample.csv.gz");
        printTen(population);
        Assert.assertFalse(population.isEmpty());
    }

    private void printTen(Set<PopulationEntryDto> populationEntryDtos) {
        Iterator<PopulationEntryDto> itr = populationEntryDtos.iterator();
        for (int i = 0; i < 10 && itr.hasNext(); i++) {
            PopulationEntryDto populationEntryDto = itr.next();
            System.out.print(populationEntryDto.getFirstName());
            System.out.print(" ");
            System.out.print(populationEntryDto.getFirstName());
            System.out.print(" ");
            System.out.print(populationEntryDto.getSiblings());
            System.out.print(" ");
            System.out.print(populationEntryDto.getSanitizedFavouriteFood());
            System.out.print(" ");
            System.out.print(populationEntryDto.getBirthTimezone());
            System.out.print(" ");
            System.out.print(populationEntryDto.getBirthTimeStamp());
            System.out.println();
        }
    }
}