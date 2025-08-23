package com.manager.finance.log;

import lombok.Getter;

import jakarta.validation.constraints.NotNull;

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
    private final String getDTOFromCache;
    private final String loadEntityFromDatabase;
    private final String saveDTOToCache;

    public CrudLogConstants(@NotNull String entityTypeName) {
        inputNewDTO = String.format("Input data for new %s is: {}", entityTypeName);
        listFiltered = String.format("List filtered %s: {}", entityTypeName);
        inputDTOToChangeEntity = String.format("Input DTO to change %s is: {}, old %s was: {} ", entityTypeName, entityTypeName);
        saveEntityToDatabase = String.format("%s was saved to database {}", entityTypeName);
        updateEntityToDatabase = String.format("%s was updated to database {}", entityTypeName);
        deleteEntityFromDatabase = String.format("%s was deleted from database {}", entityTypeName);
        inputEntityForDelete = String.format("Input entity for delete %s is: {}", entityTypeName);
        outputDTOAfterMapping = String.format("Output %s DTO after mapping is: {}", entityTypeName);
        getDTOFromCache = String.format("The %s DTO was getting from the cache: {}", entityTypeName);
        loadEntityFromDatabase = String.format("The %s entity was loaded from database: {}", entityTypeName);
        saveDTOToCache = String.format("The %s DTO was saved to cache: {}", entityTypeName);
    }

}
