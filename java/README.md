# Tasks for hiring prospects

You will need to have java 1.8 installed as well as maven 3.

To build this project, run the following from the root of the project:

```
mvn clean package
```

To run the program, run the following bash script, also found at the root of the project:

```
./stats_extractor.sh <path/to/json_or_csv>
```

Example csv file content:

```
first_name,last_name,siblings,favourite_food,birth_timezone,birth_timestamp
DELIA,MCCRAE,5,chicken,−08:00,601605300000
EUGENE,VANDERSTEEN,2,Yogurt,+01:00,853371780000
BERNARDINA,STWART,1,Mozzarella cheese,+10:30,285926100000
```

Example json file content:
```
[
{ "first_name": "LEONEL", "last_name": "FERREL", "siblings": "1", "favourite_food": "Meatballs", "birth_timezone": "−01:00", "birth_timestamp": "917172960000" },
{ "first_name": "SHANNA", "last_name": "HILYER", "siblings": "5", "favourite_food": "Meatballs", "birth_timezone": "−05:00", "birth_timestamp": "884072160000" },
{ "first_name": "CARLI", "last_name": "NEWKIRK", "siblings": "5", "favourite_food": "Candy", "birth_timezone": "+01:00", "birth_timestamp": "600794820000" }
]
```

The application will parse the specified file and print out the following information:

* Average number of siblings (round up)
* Top 3 favourite foods and the number of people who like them
* How many people were born in each month of the year (uses the month of each person's respective timezone of birth)


Example output :
```
Average siblings: 2
Three favourite foods: pizza (74), Meatballs (36), Ice Cream (33)
Birth Months: January (654), February (45), March (38), April (28), May (11), June (16), July (13), August (7), September (32), October (5), November (30), December (31)
```

We strongly encourage you to:
* test your code;
* use 3rd party libraries for parsing (ie: fasterxml and/or json) - see pom.xml;
* take readability into account (comments are appreciated!);
* take performance into account;
* ensure that it runs from the command line outside of your preferred IDE.

Assumptions you can make:
* The json and csv formats are valid and have all the fields required.
* The file extension can be relied on to indicate what the type of file.

Bonus:
* Your code can handle gzip files as well as regular files.


# David's submission notes:

## My assumptions on the data:
```
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
```
## Thoughts on space and performance:

As of now, this program works fine if the datasets can be stored into memory. Which given the test data
it seems likely.

But imagine we were trying to run this on about 7 billion entries (rough population of Earth). Given that for 50,000
entries, the population.csv was 2.3MB. Then for 7 billion entries that's about 322GB of data.

That's okay because this program can be tuned quickly to calculate the running totals of the results (I've coded it so
each line can be summed into the PopulationResult line by line, see PopulationCalcUtil.addPopulationDtoToResult(..).

However, the downfall is that this program uses a Set to detect for duplicates, which requires the data be stored
in memory. A larger-scale de-duplication strategy would probably involve the use of a large database or distributed 
file system to store the hashes of the data entries and references to the original data entry. This might need the raw data
to have unique identifiers on each row.

## To build and run unit tests:
```
mvn clean package
```

## Sample run configurations:
```
./stats_extractor.sh ./src/main/resources/population.csv
./stats_extractor.sh ./src/main/resources/population.json
./stats_extractor.sh ./src/main/resources/population_large.csv.gz
./stats_extractor.sh ./src/main/resources/population_large.json.gz

./stats_extractor.sh <ABSOLUTE_PATH_TO_FILE>
```

## Unit testing
Unit tests are also available the test directory that test basic file parsing and IO, 
individual calculations and printing of population results. To run them use:
```
mvn test
```


