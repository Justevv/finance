package com.manager.user.infrastructure.adapter.out.persistence.mapper;

import com.manager.user.domain.model.UserModel;
import com.manager.user.domain.model.VerificationModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.EmailVerificationEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.PhoneVerificationEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PhoneVerificationEntityMapper implements EntityMapper<PhoneVerificationEntity, VerificationModel> {
    private final EntityMapper<UserEntity, UserModel> userMapper;

    @Override
    public VerificationModel toModel(PhoneVerificationEntity dto) {
        if (dto == null) {
            return null;
        }
        return VerificationModel.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .expireTime(dto.getExpireTime())
                .user(userMapper.toModel(dto.getUser()))
                .build();
    }

    @Override
    public PhoneVerificationEntity toEntity(VerificationModel dto) {
        if (dto == null) {
            return null;
        }
        return PhoneVerificationEntity.builder()
                .id(dto.id())
                .code(dto.code())
                .expireTime(dto.expireTime())
                .user(userMapper.toEntity(dto.user()))
                .build();
    }

}
