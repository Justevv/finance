package com.manager.finance.service;

import com.manager.finance.dto.PlaceDTO;
import com.manager.finance.dto.response.PlaceResponseDTO;
import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.helper.UserHelper;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.repository.PlaceRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PlaceService implements CrudService<PlaceEntity, PlaceDTO, PlaceResponseDTO> {
    @Getter
    private final String entityTypeName;
    private final PlaceRepository placeRepository;
    private final CrudLogConstants crudLogConstants;
    @Getter
    private final ModelMapper mapper;
    private final UserHelper userHelper;

    public PlaceService(PlaceRepository placeRepository, ModelMapper mapper, UserHelper userHelper) {
        this.placeRepository = placeRepository;
        this.mapper = mapper;
        this.userHelper = userHelper;
        entityTypeName = "place";
        crudLogConstants = new CrudLogConstants(entityTypeName);
    }

    @Override
    public PlaceResponseDTO get(PlaceEntity entity) {
        return convertEntityToResponseDTO(entity);
    }

    @Override
    public PlaceResponseDTO get(UUID entity, Principal principal) {
//        return convertEntityToResponseDTO(entity);
        return null;
    }

    public List<PlaceResponseDTO> getAll(Principal principal) {
        var user = userHelper.getUser(principal);
        var categoryEntity = placeRepository.findByUser(user);
        log.debug(crudLogConstants.getListFiltered(), categoryEntity);
        return categoryEntity.stream().map(this::convertEntityToResponseDTO).toList();
    }

    @Override
    public PlaceResponseDTO create(Principal principal, PlaceDTO placeDTO) {
        var place = saveAndGet(principal, placeDTO);
        return convertEntityToResponseDTO(place);
    }

    private PlaceEntity saveAndGet(Principal principal, PlaceDTO placeDTO) {
        log.debug(crudLogConstants.getInputNewDTO(), placeDTO);
        var place = getMapper().map(placeDTO, PlaceEntity.class);
        place.setGuid(UUID.randomUUID());
        place.setUser(userHelper.getUser(principal));
        placeRepository.save(place);
        log.info(crudLogConstants.getSaveEntityToDatabase(), place);
        return place;
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

    public PlaceEntity getOrCreate(Principal principal, PlaceDTO placeDTO) {
        if (placeDTO == null) {
            return null;
        } else if (placeDTO.getGuid() != null) {
            var category = placeRepository.findById(placeDTO.getGuid());
            if (category.isPresent()) {
                return category.get();
            }
        } else if (placeDTO.getName() != null) {
            var category = placeRepository.findByName(placeDTO.getName());
            return category.orElseGet(() -> saveAndGet(principal, placeDTO));
        }
        return null;
    }
}


