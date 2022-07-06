package com.manager.finance.model;

import com.manager.finance.dto.PlaceDTO;
import com.manager.finance.dto.response.PlaceResponseDTO;
import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.helper.UserHelper;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.repository.PlaceRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
public class PlaceModel implements CrudModel<PlaceEntity, PlaceDTO, PlaceResponseDTO> {
    @Getter
    private final String entityTypeName;
    private final PlaceRepository placeRepository;
    private final CrudLogConstants crudLogConstants;
    @Getter
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private UserHelper userHelper;

    public PlaceModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        entityTypeName = "place";
        crudLogConstants = new CrudLogConstants(entityTypeName);
    }

    @Override
    public PlaceResponseDTO get(PlaceEntity entity) {
        return convertEntityToResponseDTO(entity);
    }

    public List<PlaceResponseDTO> getAll(Principal principal) {
        var user = userHelper.getUser(principal);
        var categoryEntity = placeRepository.findByUser(user);
        log.debug(crudLogConstants.getListFiltered(), categoryEntity);
        return categoryEntity.stream().map(this::convertEntityToResponseDTO).toList();
    }

    @Override
    public PlaceResponseDTO create(Principal principal, PlaceDTO placeDTO) {
        log.debug(crudLogConstants.getInputNewDTO(), placeDTO);
        var place = getMapper().map(placeDTO, PlaceEntity.class);
        place.setUser(userHelper.getUser(principal));
        placeRepository.save(place);
        log.info(crudLogConstants.getSaveEntityToDatabase(), place);
        return convertEntityToResponseDTO(place);
    }

    @Override
    public PlaceResponseDTO update(PlaceEntity place, PlaceDTO placeDTO) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), placeDTO, place);
        getMapper().map(placeDTO, place);
        placeRepository.save(place);
        log.info(crudLogConstants.getUpdateEntityToDatabase(), place);
        return convertEntityToResponseDTO(place);
    }

    @Override
    public Void delete(PlaceEntity categoryEntity) {
        log.debug(crudLogConstants.getDeleteEntityFromDatabase(), categoryEntity);
        placeRepository.delete(categoryEntity);
        return null;
    }

    private PlaceResponseDTO convertEntityToResponseDTO(PlaceEntity place) {
        var responseDTO = mapper.map(place, PlaceResponseDTO.class);
        log.debug(crudLogConstants.getOutputDTOAfterMapping(), responseDTO);
        return responseDTO;
    }

}


