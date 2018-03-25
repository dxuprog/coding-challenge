package com.interset.interview;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class Runner {

    /**
     * Assumptions from implementer (David Xu):
     *
     * 1. Entries from the data with identical rows will be treated as duplicates. The duplicate will not be added
     *    otherwise it will skew data results such as favourite food counts.
     *    I will be using all the values as keys. Any deviation from a single field will be treated as a different person.
     *
     * 2. Favorite Foods are case insensitive and have whitespace ignored.
     *    "Steak", "steak" and "steak " will be counted as the same thing.
     *
     * 3. A caveat to #1 and #2. #2 will be done first when considering #1's rules about identical data. E.g.
     * ["John", "Doe", "Steak", "4141425"] and ["John", "Doe", "steak ", "4141425"] are the same person.
     *      I will only be loading one of these entries. The other will be tossed.
     *
     * 4. I'm going to assume first name and last name are always upper cased.
     *
     */

    /**
     * This is main method which is starting point for this application.
     * It requires 1 arguments to run successfully.
     *
     * @param: args[0] : Path to JSON or CSV file to read. Also accepts GZIP's version of the JSON or CSV file.
     *
     * The JSON and CSV files must contain the following fields:
     *  name, siblings, favourite_food, birth_timezone, birth_timestamp
     *
     * This application parses the files and prints out the following information:
     *       - Average number of siblings (round up)
     *       - Top 3 favourite foods
     *       - How many people were born in each month of the year (uses the month of each person's respective timezone of birth)
     *
     */
    public static void main(String args[]) throws Exception {

        if (args.length != 1) {
            System.out.println("We currently only expect 1 argument! A path to a JSON or CSV file to read.");
            System.exit(1);
        }
        Runner runner = new Runner();
        String filePath = args[0];

        // Map the data into POJOs and Eliminate duplicates (see notes above for assumptions on data)
        Set<PopulationEntryDto> result = runner.parsePopulation(filePath);

        // Calculate the average siblings, top foods, and birth frequencies by month.
        PopulationResult populationResult = PopulationCalcUtil.generateResult(result);

        // Print out the results.
        PopulationCalcUtil.printResults(populationResult);
    }

    /**
     * Creates an input stream to the data, and routes the data to the correct parser based on file extension.
     *
     * @param filePath Path to file. (Absolute, or relative to working directory)
     * @return Mapped data.
     * @throws Exception
     */
    public Set<PopulationEntryDto> parsePopulation(String filePath) throws Exception {
        int i = filePath.lastIndexOf('.');
        String ext = filePath.substring(i + 1);

        InputStream inputStream =  new FileInputStream(filePath);


        if (ext.equalsIgnoreCase("GZ")) {
            String[] split = filePath.split("\\.");
            ext = split[split.length - 2];
            inputStream = new GZIPInputStream(inputStream);
        }

        Set<PopulationEntryDto> result = null;
        try {
            if (ext.equalsIgnoreCase("CSV")) {
                result = parseCsv(inputStream);
            } else if (ext.equalsIgnoreCase("JSON")) {
                result = parseJson(inputStream);
            }
        } catch (Exception e) {
            throw new Exception("Failed to parse file", e);
        } finally {
            inputStream.close();
        }


        if (result == null) {
            throw new Exception("Could not read in file");
        }
        return result;
    }

    /**
     * Parse the CSV using Jackson.
     *
     * @param inputStream Input stream to CSV file.
     * @return Mapped data.
     * @throws IOException
     */
    public static Set<PopulationEntryDto> parseCsv(InputStream inputStream) throws IOException {

        // Ignore the header.
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper
                .schemaFor(PopulationEntryDto.class)
                .withSkipFirstDataRow(true);

        MappingIterator<PopulationEntryDto> it = mapper
                .readerFor(PopulationEntryDto.class)
                .with(schema)
                .readValues(inputStream);

        // Put the data into a set to avoid duplicates.
        Set<PopulationEntryDto> populationEntryDtos = new HashSet<>();
        while (it.hasNext()) {
            PopulationEntryDto populationEntryDto = it.next();
            populationEntryDtos.add(populationEntryDto);

            // TODO: If performance is a major issue, we could start calling
            // PopulationCalcUtil.addPopulationDtoToResult(..) to generate the results on the fly and avoid doing a
            // second pass of all the data later on. We just need to be aware of dupes.
        }
        return populationEntryDtos;
    }

    /**
     * Parse the JSON using Jackson databind.
     *
     * @param inputStream Input stream to JSON file.
     * @return Mapped data.
     * @throws IOException
     */
    public static Set<PopulationEntryDto> parseJson(InputStream inputStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Rely on Jackson's object model of PopulationEntryDto to do the mapping for us.
        List<PopulationEntryDto> data = mapper.readValue(inputStream, new TypeReference<List<PopulationEntryDto>>(){});

        // Put the data into a set to avoid duplicates.
        Set<PopulationEntryDto> populationEntryDtos = new HashSet<>(data);

        // TODO: If performance is a major issue, we could start looking at a iterative way to retrieve each data entry
        // and use PopulationCalcUtil.addPopulationDtoToResult(..) to generate the results on the fly to avoid doing a
        // second pass of all the data later on. We just need to be aware of dupes.

        return populationEntryDtos;
    }
}
