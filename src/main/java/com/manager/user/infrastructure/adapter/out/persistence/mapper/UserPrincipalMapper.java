package com.manager.user.infrastructure.adapter.out.persistence.mapper;

import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserPrincipalMapper {

    public UserModel toModel(UserEntity dto) {
        if (dto == null) {
            return null;
        }
        return UserModel.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .isEmailConfirmed(dto.isEmailConfirmed())
                .isPhoneConfirmed(dto.isPhoneConfirmed())
                .roles(dto.getRoles())
                .build();
    }

    public UserModel toModel(Principal principal) {
        log.debug("input principal is {}", principal);
        if (principal instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
            var user = usernamePasswordAuthenticationToken.getPrincipal();
            if (user instanceof UserModel userModel) {
                log.debug("Current user is {}", userModel);
                return userModel;
            }
        }

        return null;
    }
}
