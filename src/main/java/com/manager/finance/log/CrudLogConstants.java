package com.manager.finance.log;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CrudLogConstants {
    private final String listFiltered;
    private final String inputNewDTO;
    private final String inputDTOToChangeEntity;
    private final String saveEntityToDatabase;
    private final String updateEntityToDatabase;
    private final String deleteEntityFromDatabase;
    private final String inputEntityForDelete;
    private final String outputDTOAfterMapping;

    public CrudLogConstants(@NotNull String property) {
        inputNewDTO = String.format("Input data for new %s is: {}", property);
        listFiltered = String.format("List filtered %s: {}", property);
        inputDTOToChangeEntity = String.format("Input DTO to change %s is: {}, old %s was: {} ", property, property);
        saveEntityToDatabase = String.format("%s was saved to database {}", property);
        updateEntityToDatabase = String.format("%s was updated to database {}", property);
        deleteEntityFromDatabase = String.format("%s was deleted from database {}", property);
        inputEntityForDelete = String.format("Input entity for delete %s is: {}", property);
        outputDTOAfterMapping = String.format("Output %s DTO after mapping is: {}", property);
    }

}
