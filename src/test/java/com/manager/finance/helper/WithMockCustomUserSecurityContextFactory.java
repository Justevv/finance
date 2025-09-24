package com.manager.finance.helper;

import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.user.domain.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Autowired
    private UserPrepareHelper userPrepareHelper;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUserAnnotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserModel principal = userPrepareHelper.createUserModel();

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, principal.password(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}