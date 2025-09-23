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
    public PasswordResetTokenModel toModel(PasswordResetTokenEntity dto) {
        if (dto == null) {
            return null;
        }
        return PasswordResetTokenModel.builder()
                .id(dto.getId())
                .token(dto.getToken())
                .expireTime(dto.getExpireTime())
                .user(userMapper.toModel(dto.getUser()))
                .build();
    }

    @Override
    public PasswordResetTokenEntity toEntity(PasswordResetTokenModel dto) {
        if (dto == null) {
            return null;
        }
        return PasswordResetTokenEntity.builder()
                .id(dto.id())
                .token(dto.token())
                .expireTime(dto.expireTime())
                .user(userMapper.toEntity(dto.user()))
                .build();
    }

}
