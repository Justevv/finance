package com.manager.finance.service;

import com.manager.finance.dto.PlaceDTO;
import com.manager.finance.dto.response.PlaceResponseDTO;
import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.exception.EntityNotFoundException;
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
public class PlaceService implements CreateReadService<PlaceDTO, PlaceResponseDTO> {
    private static final String ENTITY_TYPE_NAME = "place";
    private final PlaceRepository placeRepository;
    private final CrudLogConstants crudLogConstants;
    @Getter
    private final ModelMapper mapper;
    private final UserHelper userHelper;

    public PlaceService(PlaceRepository placeRepository, ModelMapper mapper, UserHelper userHelper) {
        this.placeRepository = placeRepository;
        this.mapper = mapper;
        this.userHelper = userHelper;
        crudLogConstants = new CrudLogConstants(ENTITY_TYPE_NAME);
    }

    @Override
    public PlaceResponseDTO get(UUID id, Principal principal) {
        var category = placeRepository.findById(id);
        if (category.isPresent()) {
            return convertEntityToResponseDTO(category.get());
        } else {
            throw new EntityNotFoundException(ENTITY_TYPE_NAME, id);
        }
    }

    public List<PlaceResponseDTO> getAll(Principal principal) {
        var categoryEntity = placeRepository.findAll();
        log.debug(crudLogConstants.getListFiltered(), categoryEntity);
        return categoryEntity.stream().map(this::convertEntityToResponseDTO).toList();
    }

    @Override
    public PlaceResponseDTO create(Principal principal, PlaceDTO placeDTO) {
        var place = saveAndGet(placeDTO);
        return convertEntityToResponseDTO(place);
    }

    public PlaceEntity getOrCreate(PlaceDTO placeDTO) {
        if (placeDTO == null) {
            return null;
        } else if (placeDTO.getId() != null) {
            var category = placeRepository.findById(placeDTO.getId());
            if (category.isPresent()) {
                return category.get();
            }
        } else if (placeDTO.getName() != null) {
            var category = placeRepository.findByName(placeDTO.getName());
            return category.orElseGet(() -> saveAndGet(placeDTO));
        }
        return null;
    }

    private PlaceEntity saveAndGet(PlaceDTO placeDTO) {
        log.debug(crudLogConstants.getInputNewDTO(), placeDTO);
        var place = getMapper().map(placeDTO, PlaceEntity.class);
       placeRepository.save(place);
        log.info(crudLogConstants.getSaveEntityToDatabase(), place);
        return place;
    }

    private PlaceResponseDTO convertEntityToResponseDTO(PlaceEntity place) {
        var responseDTO = mapper.map(place, PlaceResponseDTO.class);
        log.debug(crudLogConstants.getOutputDTOAfterMapping(), responseDTO);
        return responseDTO;
    }

}


