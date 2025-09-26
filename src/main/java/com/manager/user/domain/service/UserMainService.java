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
public class UserMainService {
    private static final String USER_LOG_NAME = "user";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(USER_LOG_NAME);
    private final UserRepository userRepository;

    @Transactional
    @TrackExecutionTime
    public void delete(UserModel user) {
        log.debug(crudLogConstants.getInputEntityForDelete(), user);
        userRepository.delete(user);
        log.info(crudLogConstants.getDeleteEntityFromDatabase(), user);
    }

}
