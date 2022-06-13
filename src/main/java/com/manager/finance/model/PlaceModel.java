package com.manager.finance.model;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.PlaceDTO;
import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.repository.PlaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
public class PlaceModel extends CrudModel<PlaceEntity, PlaceDTO> {
    private static final String PLACE = "place";
    private final PlaceRepository placeRepository;
    private final LogConstants logConstants = new LogConstants(PLACE);

    public PlaceModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<PlaceEntity> get() {
        List<PlaceEntity> categoryEntity = placeRepository.findAll();

        log.debug(logConstants.getListFiltered(), categoryEntity);
        return categoryEntity;
    }

    @Override
    public PlaceEntity create(PlaceDTO placeDTO, Principal principal) {
        log.debug(logConstants.getInputDataNew(), placeDTO);
        var place = getMapper().map(placeDTO, PlaceEntity.class);
        place.setUser(getUserRepository().findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
        placeRepository.save(place);
        log.info(logConstants.getSaveToDatabase(), place);
        return place;
    }

    @Override
    public PlaceEntity update(PlaceEntity placeEntity, PlaceDTO placeDTO) {
        log.debug(logConstants.getInputDataToChange(), placeEntity, placeDTO);
        getMapper().map(placeDTO, placeEntity);
        placeRepository.save(placeEntity);
        log.info(logConstants.getUpdatedToDatabase(), placeEntity);
        return placeEntity;
    }

    @Override
    public Void delete(PlaceEntity categoryEntity) {
        log.debug(logConstants.getInputDataForDelete(), categoryEntity);
        placeRepository.delete(categoryEntity);
        log.info(logConstants.getDeletedFromDatabase(), categoryEntity);
        return null;
    }

}


