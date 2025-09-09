package com.manager.user.infrastructure.adapter.in.rest.mapper;

import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserRequestDTO;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.UserResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserDtoMapper implements DtoMapper<UserRequestDTO, UserResponseDTO, UserModel> {

    @Override
    public UserModel toModel(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return UserModel.builder()
                .username(dto.username())
                .password(dto.password())
                .phone(dto.phone())
                .email(dto.email())
                .build();
    }

    @Override
    public UserResponseDTO toResponseDto(UserModel dto) {
        if (dto == null) {
            return null;
        }
        return UserResponseDTO.builder()
                .id(dto.id())
                .username(dto.username())
                .phone(dto.phone())
                .email(dto.email())
                .build();
    }

}
