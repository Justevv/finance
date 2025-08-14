package com.manager.finance.listener;

import com.manager.finance.event.ConfirmationCompleteEvent;
import com.manager.finance.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConfirmationCompleteListener {
    private final RoleRepository roleRepository;

    @EventListener
    @SneakyThrows
    public void onApplicationEvent(ConfirmationCompleteEvent event) {
        log.debug("Event start {}", event);
        var user = event.getUser();
        log.debug("Trying to set a role for the user {}", user);
        if (user.isEmailConfirmed() && user.isPhoneConfirmed()) {
            user.setRoles(Set.of(roleRepository.findByName("ROLE_USER").orElseThrow()));
            log.debug("The role has been set for the user {}", user);
        }
    }

}
