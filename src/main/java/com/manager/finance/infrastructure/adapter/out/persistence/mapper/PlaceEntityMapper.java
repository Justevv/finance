package com.manager.finance.infrastructure.adapter.out.persistence.mapper;

import com.manager.finance.domain.model.PlaceModel;
import com.manager.finance.infrastructure.adapter.in.rest.dto.request.PlaceRequestDTO;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.PlaceResponseDTO;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.PlaceEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PlaceEntityMapper implements EntityMapper<PlaceEntity, PlaceModel> {

    @Override
    public PlaceModel toModel(PlaceEntity dto){
        if (dto == null) {
            return null;
        }
        return PlaceModel.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .build();
    }

    @Override
    public PlaceEntity toEntity(PlaceModel dto){
        if (dto == null) {
            return null;
        }
        return PlaceEntity.builder()
                .id(dto.id())
                .name(dto.name())
                .address(dto.address())
                .build();
    }
}
