package com.manager.finance.model;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.PlaceDTO;
import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.repo.PlaceRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
public class PlaceModel extends CrudModel<PlaceEntity, PlaceDTO> {
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

    @Override
    public PlaceEntity create(PlaceDTO placeDTO, Principal principal) {
        log.debug(logConstants.getInputDataNew(), placeDTO);
        var place = getMapper().map(placeDTO, PlaceEntity.class);
        place.setUser(getUserRepo().findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
        placeRepo.save(place);
        log.info(logConstants.getSaveToDatabase(), place);
        return place;
    }

    @Override
    public PlaceEntity update(PlaceEntity placeEntity, PlaceDTO placeDTO) {
        log.debug(logConstants.getInputDataToChange(), placeEntity, placeDTO);
        getMapper().map(placeDTO, placeEntity);
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


