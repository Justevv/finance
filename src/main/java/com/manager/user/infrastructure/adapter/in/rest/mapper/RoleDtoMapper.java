package com.manager.user.infrastructure.adapter.in.rest.mapper;

import com.manager.user.domain.model.RoleModel;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.RoleRequestDto;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.RoleResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoleDtoMapper implements DtoMapper<RoleRequestDto, RoleResponseDto, RoleModel> {

    @Override
    public RoleModel toModel(RoleRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return RoleModel.builder()
                .name(dto.name())
                .permissions(dto.permissions())
                .build();
    }

    @Override
    public RoleResponseDto toResponseDto(RoleModel model) {
        if (model == null) {
            return null;
        }
        return RoleResponseDto.builder()
                .id(model.id())
                .name(model.name())
                .permissions(model.permissions())
                .build();
    }

}
