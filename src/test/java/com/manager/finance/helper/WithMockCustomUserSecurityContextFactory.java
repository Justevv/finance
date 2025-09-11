package com.manager.finance.helper;

import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Set;
import java.util.UUID;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Autowired
    private UserPrepareHelper userPrepareHelper;
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUserAnnotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserEntity user = userPrepareHelper.createUser();
        RoleEntity roleEntity = RoleEntity.builder()
                .name("USER")
                .permissions(Set.of(PermissionEntity.ALL_READ))
                .build();
        UserModel principal = UserModel.builder()
                .id(UUID.fromString("3a2830b2-6d06-4964-bba5-e90a29d0fcd0"))
                .username(user.getUsername())
                .phone(user.getPhone())
                .email(user.getEmail())
                .roles(Set.of(roleEntity))
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, principal.password(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}