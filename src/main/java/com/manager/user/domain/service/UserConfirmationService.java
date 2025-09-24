package com.manager.user.domain.service;

import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.application.port.out.repository.UserRepository;
import com.manager.user.domain.model.UserModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserConfirmationService {
    private static final String USER_LOG_NAME = "user";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(USER_LOG_NAME);
    private final UserRepository userRepository;


    @Transactional
    @TrackExecutionTime
    public UserModel confirmEmail(UserModel principal) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), principal);

        UserModel save = UserModel.builder()
                .id(principal.id())
                .username(principal.username())
                .password(principal.password())
                .phone(principal.phone())
                .email(principal.email())
                .isPhoneConfirmed(principal.isPhoneConfirmed())
                .isEmailConfirmed(true)
                .roles(principal.roles())
                .build();

        var saved = userRepository.save(save);
        log.info(crudLogConstants.getUpdateEntityToDatabase(), saved);
        return saved;
    }

    @Transactional
    @TrackExecutionTime
    public UserModel confirmPhone(UserModel principal) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), principal);
        var currentUser = userRepository.getById(principal.id());
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), currentUser);

        UserModel save = UserModel.builder()
                .id(currentUser.id())
                .username(currentUser.username())
                .password(currentUser.password())
                .phone(currentUser.phone())
                .email(currentUser.email())
                .isPhoneConfirmed(true)
                .isEmailConfirmed(currentUser.isEmailConfirmed())
                .roles(currentUser.roles())
                .build();

        var saved = userRepository.save(save);
        log.info(crudLogConstants.getUpdateEntityToDatabase(), saved);
        return saved;
    }

}
