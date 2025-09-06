package com.manager.finance.infrastructure.adapter.in.rest.mapper;

import com.manager.finance.domain.model.PlaceModel;
import com.manager.finance.infrastructure.adapter.in.rest.dto.request.PlaceRequestDTO;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.PlaceResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PlaceDtoMapper implements DtoMapper<PlaceRequestDTO, PlaceResponseDTO, PlaceModel> {

    @Override
    public PlaceModel toModel(PlaceRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return PlaceModel.builder()
                .id(dto.id())
                .name(dto.name())
                .address(dto.address())
                .build();
    }

    @Override
    public PlaceResponseDTO toResponseDto(PlaceModel dto) {
        if (dto == null) {
            return null;
        }
        return PlaceResponseDTO.builder()
                .id(dto.id())
                .name(dto.name())
                .address(dto.address())
                .build();
    }
}
