package com.manager.model;

import com.manager.config.LogConstants;
import com.manager.dto.PlaceDTO;
import com.manager.entity.PlaceEntity;
import com.manager.repo.PlaceRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceModel {
    private static final Logger LOGGER = LogManager.getLogger(PlaceModel.class);
    private static final String PLACE = "place";
    private final PlaceRepo placeRepo;
    private final LogConstants logConstants = new LogConstants(PLACE);

    public PlaceModel(PlaceRepo placeRepo) {
        this.placeRepo = placeRepo;
    }

    public List<PlaceEntity> get() {
        List<PlaceEntity> categoryEntity = placeRepo.findAll();

        LOGGER.debug(logConstants.getListFiltered(), categoryEntity);
        return categoryEntity;
    }

    public PlaceEntity create(PlaceDTO placeDTO) {
        LOGGER.debug(logConstants.getInputDataNew(), placeDTO);
        PlaceEntity categoryEntity = new PlaceEntity(placeDTO);
        placeRepo.save(categoryEntity);
        LOGGER.info(logConstants.getSaveToDatabase(), categoryEntity);
        return categoryEntity;
    }

    public PlaceEntity change(PlaceEntity placeEntity, PlaceDTO placeDTO) {
        LOGGER.debug(logConstants.getInputDataToChange(), placeEntity, placeDTO);
        placeEntity.setName(placeDTO.getName());
        placeEntity.setAddress(placeDTO.getAddress());
        placeRepo.save(placeEntity);
        LOGGER.info(logConstants.getUpdatedToDatabase(), placeEntity);
        return placeEntity;
    }

    public Void delete(PlaceEntity categoryEntity) {
        LOGGER.debug(logConstants.getInputDataForDelete(), categoryEntity);
        placeRepo.delete(categoryEntity);
        LOGGER.info(logConstants.getDeletedFromDatabase(), categoryEntity);
        return null;
    }

}


