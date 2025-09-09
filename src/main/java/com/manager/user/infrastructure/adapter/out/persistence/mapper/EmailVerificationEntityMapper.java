package com.manager.user.infrastructure.adapter.out.persistence.mapper;

import com.manager.user.domain.model.VerificationModel;
import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.EmailVerificationEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailVerificationEntityMapper implements EntityMapper<EmailVerificationEntity, VerificationModel> {
    private final EntityMapper<UserEntity, UserModel> userMapper;

    @Override
    public VerificationModel toModel(EmailVerificationEntity dto) {
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
    public EmailVerificationEntity toEntity(VerificationModel dto) {
        if (dto == null) {
            return null;
        }
        return EmailVerificationEntity.builder()
                .id(dto.id())
                .code(dto.code())
                .expireTime(dto.expireTime())
                .user(userMapper.toEntity(dto.user()))
                .build();
    }

}
