package com.interset.interview;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;

/**
 * Methods for handling processing the DTOs into a result. And methods for calculating the output of the results.
 *
 * TODO: We could split this into two files. One for creating the result, and one for calculating the output of the results.
 * TODO: Or we could make the calculation methods of the PopulationResult object itself.
 */
public class PopulationCalcUtil {

    public static PopulationResult generateResult(Set<PopulationEntryDto> populationEntryDtos) {
        PopulationResult populationResult = new PopulationResult();
        for (PopulationEntryDto populationEntryDto : populationEntryDtos) {
            addPopulationDtoToResult(populationResult, populationEntryDto);
        }
        return populationResult;
    }

    public static void addPopulationDtoToResult(PopulationResult populationResult, PopulationEntryDto populationEntryDto) {
        // add to sibling sum
        populationResult.addToSiblingSum(populationEntryDto.getSiblings());

        // add to Food frequency map
        String food = populationEntryDto.getSanitizedFavouriteFood();

        Map<String, Integer> favFoodFreq = populationResult.getFavouriteFoods();
        int foodCount = favFoodFreq.containsKey(food) ? favFoodFreq.get(food) : 0;
        favFoodFreq.put(food, foodCount + 1);

        // add to Birth Month frequency map
        LocalDateTime date = getLocalDateTime(populationEntryDto);
        Month birthMonth = date.getMonth();
        Map<Month, Integer> birthMonthFrequency = populationResult.getBirthMonthFrequency();
        int birthCount = birthMonthFrequency.containsKey(date.getMonth()) ? birthMonthFrequency.get(birthMonth) : 0;
        birthMonthFrequency.put(birthMonth, birthCount + 1);
    }

    public static LocalDateTime getLocalDateTime(PopulationEntryDto populationEntryDto) {
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(populationEntryDto.getBirthTimeStamp()),
                ZoneId.of(populationEntryDto.getBirthTimezone()));
        return date;
    }

    public static void printResults(PopulationResult populationResult) {
        System.out.println("Average siblings: " + calculateAverageSiblings(populationResult));
        System.out.println("Three favourite foods:" + calculateThreeFavouriteFoods(populationResult));
        System.out.println("Birth Months:" + calculateBirthMonthTotals(populationResult));
    }

    public static int calculateAverageSiblings(PopulationResult populationResult) {
        if (populationResult.getSize() == 0) return 0;
        return (int) Math.ceil(populationResult.getSiblingSum() / ((double) populationResult.getSize()));
    }

    public static String calculateThreeFavouriteFoods(PopulationResult populationResult) {
        List<String> topFoods = getTopFoods(populationResult);
        StringJoiner result = new StringJoiner(",");
        for (String topFood : topFoods) {
            result.add(" " + topFood + " (" + populationResult.getFavouriteFoods().get(topFood) + ")");
        }

        return result.toString();
    }

    public static List<String> getTopFoods(PopulationResult populationResult) {
        // We could also sort here and iterate through the top 3.
        // But since we're only grabbing the top 3, doing three passes through the unsorted array should be faster.
        List<String> topFoods = new ArrayList<>();
        for (int i = 0; i < populationResult.getSize() && i < 3; i++) {
            int maxFreq = 0;
            String maxFood = null;
            for (Map.Entry<String, Integer> entry : populationResult.getFavouriteFoods().entrySet()) {
                if (!topFoods.contains(entry.getKey()) && entry.getValue() > maxFreq) {
                    maxFreq = entry.getValue();
                    maxFood = entry.getKey();
                }
            }
            if (maxFood != null) {
                topFoods.add(maxFood);
            }
        }
        return topFoods;
    }

    public static String calculateBirthMonthTotals(PopulationResult populationResult) {
        StringJoiner result = new StringJoiner(",");
        for (Month month : Month.values()) {
            int birthFreq = populationResult.getBirthMonthFrequency().containsKey(month) ?
                    populationResult.getBirthMonthFrequency().get(month) : 0;
            result.add(" " + month.getDisplayName(TextStyle.FULL, Locale.CANADA) + " (" + birthFreq + ")");
        }
        return result.toString();
    }
}
