package com.manager.config;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class LogConstants {
    private final String updatedResponse;
    private final String savedResponse;
    private final String deletedResponse;
    private final String errorAdd;
    private final String listFiltered;
    private final String input;
    private final String inputDataNew;
    private final String errorChange;
    private final String inputDataToChange;
    private final String saveToDatabase;
    private final String updatedToDatabase;
    private final String deletedFromDatabase;
    private final String inputDataForDelete;

    public LogConstants(@NotNull String property) {
        updatedResponse = String.format("Updated response for %s is {}", property);
        savedResponse =  String.format("Saved response for %s is {}", property);
        deletedResponse =  String.format("Deleted response for %s is {}", property);
        errorAdd =  String.format("Error add %s {}", property);
        listFiltered =  String.format("List filtered %s {}", property);
        input =  String.format("Input %s {}", property);
        inputDataNew =  String.format("Input data new %s {}", property);
        errorChange =  String.format("Error change %s {}", property);
        inputDataToChange =  String.format("Input data to change %s is {}, old %s was {} ", property, property);
        saveToDatabase =  String.format("%s was saved to database {}", property);
        updatedToDatabase =  String.format("%s was updated to database {}", property);
        deletedFromDatabase =  String.format("%s was deleted from database {}", property);
        inputDataForDelete =  String.format("Input data for delete %s is {}", property);
    }

}
