package com.manager.finance.log;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LogConstants {
    public static final String LIST_FILTERED_RESPONSE_DTO = "List filtered response DTO: {}";
    public static final String INPUT_NEW_DTO = "Input data for new entity is: {}";
    public static final String INPUT_DTO_TO_CHANGE_ENTITY = "Input DTO to change entity is: {}, old entity was: {}";
    public static final String SAVE_ENTITY_TO_DATABASE = "Entity was saved to database {}";
    public static final String UPDATE_ENTITY_TO_DATABASE = "Entity was updated to database {}";
    public static final String DELETE_ENTITY_FROM_DATABASE = "Entity was deleted from database {}";
    public static final String DELETE_ENTITY_FROM_DATABASE_BY_GUID = "Entity {} with guid [{}] was deleted from database ";
    public static final String INPUT_ENTITY_FOR_DELETE = "Input entity for delete is: {}";
    public static final String OUTPUT_DTO_AFTER_MAPPING = "Output DTO after mapping is: {}";
    public static final String GET_DTO_FROM_CACHE = "The DTO was getting from the cache: {}";
    public static final String LOAD_ENTITY_FROM_DATABASE = "The entity was loaded from database: {}";
    public static final String SAVE_DTO_TO_CACHE = "The  DTO was saved to cache: {}";

}
