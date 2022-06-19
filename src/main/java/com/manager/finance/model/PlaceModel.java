package com.manager.finance.model;

import com.manager.finance.config.CrudLogConstants;
import com.manager.finance.dto.PlaceDTO;
import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.repository.PlaceRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
public class PlaceModel extends CrudModel<PlaceEntity, PlaceDTO> {
    private static final String PLACE = "place";
    private final PlaceRepository placeRepository;
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(PLACE);
    @Getter
    private final ModelMapper mapper = new ModelMapper();

    public PlaceModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<PlaceEntity> getAll(Principal principal) {
        var user = getUserRepository().findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<PlaceEntity> categoryEntity = placeRepository.findByUser(user);
        log.debug(crudLogConstants.getListFiltered(), categoryEntity);
        return categoryEntity;
    }

    @Override
    public PlaceEntity create(PlaceDTO placeDTO, Principal principal) {
        log.debug(crudLogConstants.getInputDataNew(), placeDTO);
        var place = getMapper().map(placeDTO, PlaceEntity.class);
        place.setUser(getUserRepository().findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
        placeRepository.save(place);
        log.info(crudLogConstants.getSaveToDatabase(), place);
        return place;
    }

    @Override
    public PlaceEntity update(PlaceEntity placeEntity, PlaceDTO placeDTO) {
        log.debug(crudLogConstants.getInputDataToChange(), placeEntity, placeDTO);
        getMapper().map(placeDTO, placeEntity);
        placeRepository.save(placeEntity);
        log.info(crudLogConstants.getUpdatedToDatabase(), placeEntity);
        return placeEntity;
    }

    @Override
    public Void delete(PlaceEntity categoryEntity) {
        log.debug(crudLogConstants.getInputDataForDelete(), categoryEntity);
        placeRepository.delete(categoryEntity);
        log.info(crudLogConstants.getDeletedFromDatabase(), categoryEntity);
        return null;
    }

}


