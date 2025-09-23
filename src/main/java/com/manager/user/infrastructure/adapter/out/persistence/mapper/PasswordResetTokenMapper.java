package com.manager.user.infrastructure.adapter.out.persistence.mapper;

import com.manager.user.domain.model.PasswordResetTokenModel;
import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.PasswordResetTokenEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PasswordResetTokenMapper implements EntityMapper<PasswordResetTokenEntity, PasswordResetTokenModel> {
    private final EntityMapper<UserEntity, UserModel> userMapper;

    @Override
    public PasswordResetTokenModel toModel(PasswordResetTokenEntity entity) {
        if (entity == null) {
            return null;
        }
        return PasswordResetTokenModel.builder()
                .id(entity.getId())
                .token(entity.getToken())
                .expireTime(entity.getExpireTime())
                .user(userMapper.toModel(entity.getUser()))
                .status(entity.getStatus())
                .build();
    }

    @Override
    public PasswordResetTokenEntity toEntity(PasswordResetTokenModel model) {
        if (model == null) {
            return null;
        }
        return PasswordResetTokenEntity.builder()
                .id(model.id())
                .token(model.token())
                .expireTime(model.expireTime())
                .user(userMapper.toEntity(model.user()))
                .status(model.status())
                .build();
    }

}
