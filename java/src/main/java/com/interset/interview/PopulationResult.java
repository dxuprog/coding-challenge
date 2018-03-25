package com.interset.interview;

import java.time.Month;
import java.util.*;

/**
 * Stores running results of population data that are used in the result calculations.
 *
 * (In other words the aggregation of entry data)
 */
public class PopulationResult {

    private int siblingSum = 0;
    private int size = 0;

    Map<String, Integer> favouriteFoods = new HashMap<>();
    Map<Month, Integer> birthMonthFrequency = new HashMap<>();

    public void addToSiblingSum(int siblings) {
        siblingSum += siblings;
        size++;
    }

    public int getSiblingSum() {
        return siblingSum;
    }

    public Map<String, Integer> getFavouriteFoods() {
        return favouriteFoods;
    }

    public Map<Month, Integer> getBirthMonthFrequency() {
        return birthMonthFrequency;
    }

    public int getSize() {
        return size;
    }
}
