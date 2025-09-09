package com.manager.finance.domain.service;

import com.manager.finance.application.port.in.PlaceUseCase;
import com.manager.finance.application.port.out.repository.PlaceRepository;
import com.manager.finance.domain.model.PlaceModel;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.PlaceResponseDTO;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.PlaceEntity;
import com.manager.finance.log.CrudLogConstants;
import com.manager.user.helper.UserHelper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlaceService implements PlaceUseCase {
    private static final String ENTITY_TYPE_NAME = "place";
    private final PlaceRepository placeRepository;
    private CrudLogConstants crudLogConstants;
    @Getter
    private final ModelMapper mapper;
    private final UserHelper userHelper;

    @PostConstruct
    public void init() {
        crudLogConstants = new CrudLogConstants(ENTITY_TYPE_NAME);
    }


    @Override
    public PlaceModel get(UUID id, Principal principal) {
        return placeRepository.getById(id);
    }

    public List<PlaceModel> getAll(Principal principal) {
        var categoryEntity = placeRepository.findAll();
        log.debug(crudLogConstants.getListFiltered(), categoryEntity);
        return categoryEntity;
    }

    @Override
    public PlaceModel create(Principal principal, PlaceModel placeModel) {
        var place = getOrCreate(placeModel);
        return place;
    }

    public PlaceModel getOrCreate(PlaceModel inputPlace) {
        if (inputPlace == null) {
            return null;
        } else if (inputPlace.id() != null) {
            var currentPlace = placeRepository.findById(inputPlace.id());
            if (currentPlace.isPresent()) {
                return currentPlace.get();
            }
        } else if (inputPlace.name() != null) {
            var currentPlace = placeRepository.findByName(inputPlace.name());
            var save = PlaceModel.builder()
                    .id(UUID.randomUUID())
                    .address(inputPlace.address())
                    .name(inputPlace.name())
                    .build();
            return currentPlace.orElseGet(() -> saveAndGet(save));
        }
        return null;
    }

    private PlaceModel saveAndGet(PlaceModel placeModel) {
        log.debug(crudLogConstants.getInputNewDTO(), placeModel);
        var saved = placeRepository.save(placeModel);
        log.info(crudLogConstants.getSaveEntityToDatabase(), saved);
        return saved;
    }

    private PlaceResponseDTO convertEntityToResponseDTO(PlaceEntity place) {
        var responseDTO = mapper.map(place, PlaceResponseDTO.class);
        log.debug(crudLogConstants.getOutputDTOAfterMapping(), responseDTO);
        return responseDTO;
    }

}


