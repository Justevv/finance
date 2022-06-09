package com.manager.finance.model;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.PlaceDTO;
import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.repo.PlaceRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PlaceModel implements CrudModel<PlaceEntity, PlaceDTO> {
    private static final String PLACE = "place";
    private final PlaceRepo placeRepo;
    private final LogConstants logConstants = new LogConstants(PLACE);

    public PlaceModel(PlaceRepo placeRepo) {
        this.placeRepo = placeRepo;
    }

    public List<PlaceEntity> get() {
        List<PlaceEntity> categoryEntity = placeRepo.findAll();

        log.debug(logConstants.getListFiltered(), categoryEntity);
        return categoryEntity;
    }

    public PlaceEntity create(PlaceDTO placeDTO) {
        log.debug(logConstants.getInputDataNew(), placeDTO);
        PlaceEntity categoryEntity = new PlaceEntity(placeDTO);
        placeRepo.save(categoryEntity);
        log.info(logConstants.getSaveToDatabase(), categoryEntity);
        return categoryEntity;
    }

    @Override
    public PlaceEntity update(PlaceEntity placeEntity, PlaceDTO placeDTO) {
        log.debug(logConstants.getInputDataToChange(), placeEntity, placeDTO);
        placeEntity.setName(placeDTO.getName());
        placeEntity.setAddress(placeDTO.getAddress());
        placeRepo.save(placeEntity);
        log.info(logConstants.getUpdatedToDatabase(), placeEntity);
        return placeEntity;
    }

    @Override
    public Void delete(PlaceEntity categoryEntity) {
        log.debug(logConstants.getInputDataForDelete(), categoryEntity);
        placeRepo.delete(categoryEntity);
        log.info(logConstants.getDeletedFromDatabase(), categoryEntity);
        return null;
    }

}


