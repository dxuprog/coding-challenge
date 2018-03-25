package com.interset.interview;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

/**
 * Object model of incoming data. Works with both Jackson databind (JSON) and Jackson dataformat (csv),
 */
@JsonPropertyOrder({"first_name", "last_name", "siblings", "favourite_food", "birth_timezone", "birth_timestamp"})
public class PopulationEntryDto {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("siblings")
    private int siblings;

    @JsonProperty("favourite_food")
    private String favouriteFood;

    @JsonProperty("birth_timezone")
    private String birthTimezone;

    @JsonProperty("birth_timestamp")
    private long birthTimeStamp;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getSiblings() {
        return siblings;
    }

    public void setSiblings(int siblings) {
        this.siblings = siblings;
    }

    public String getFavouriteFood() {
        return favouriteFood;
    }

    public void setFavouriteFood(String favouriteFood) {
        this.favouriteFood = favouriteFood;
    }

    /**
     * Sanitized version.
     *
     * @return the Property that has been trimmed and lower cased.
     */
    public String getSanitizedFavouriteFood() {
        // trim whitespace and lowercase all for food
        String result  = getFavouriteFood().trim();
        result = result.toLowerCase();
        return result;
    }

    public String getBirthTimezone() {
        return birthTimezone;
    }

    public void setBirthTimezone(String birthTimezone) {
        this.birthTimezone = birthTimezone;
    }

    public long getBirthTimeStamp() {
        return birthTimeStamp;
    }

    public void setBirthTimeStamp(long birthTimeStamp) {
        this.birthTimeStamp = birthTimeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PopulationEntryDto that = (PopulationEntryDto) o;
        return siblings == that.siblings &&
                birthTimeStamp == that.birthTimeStamp &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(getSanitizedFavouriteFood(), that.getSanitizedFavouriteFood()) &&
                Objects.equals(birthTimezone, that.birthTimezone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, siblings, getSanitizedFavouriteFood(), birthTimezone, birthTimeStamp);
    }
}

