package com.manager.user.infrastructure.adapter.out.persistence.mapper;

import com.manager.user.domain.model.RoleModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoleEntityMapper implements EntityMapper<RoleEntity, RoleModel> {

    @Override
    public RoleModel toModel(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        return RoleModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .permissions(entity.getPermissions())
                .build();
    }

    @Override
    public RoleEntity toEntity(RoleModel model) {
        if (model == null) {
            return null;
        }
        return RoleEntity.builder()
                .id(model.id())
                .name(model.name())
                .permissions(model.permissions())
                .build();
    }

}
