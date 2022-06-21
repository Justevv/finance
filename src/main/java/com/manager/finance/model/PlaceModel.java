package com.manager.finance.model;

import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.dto.PlaceDTO;
import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.repository.PlaceRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ModelMapper mapper;

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
        log.debug(crudLogConstants.getInputNewDTO(), placeDTO);
        var place = getMapper().map(placeDTO, PlaceEntity.class);
        place.setUser(getUserRepository().findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
        placeRepository.save(place);
        log.info(crudLogConstants.getSaveEntityToDatabase(), place);
        return place;
    }

    @Override
    public PlaceEntity update(PlaceEntity place, PlaceDTO placeDTO) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), placeDTO, place);
        getMapper().map(placeDTO, place);
        placeRepository.save(place);
        log.info(crudLogConstants.getUpdateEntityToDatabase(), place);
        return place;
    }

    @Override
    public Void delete(PlaceEntity categoryEntity) {
        log.debug(crudLogConstants.getDeleteEntityFromDatabase(), categoryEntity);
        placeRepository.delete(categoryEntity);
        return null;
    }

}


