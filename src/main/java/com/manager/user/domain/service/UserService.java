package com.manager.user.domain.service;

import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.application.port.out.repository.UserRepository;
import com.manager.user.domain.model.UserModel;
import com.manager.user.exception.UserAlreadyExistException;
import com.manager.user.exception.UserNotFoundException;
import com.manager.user.helper.UserHelper;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.OldUserResponseDTO;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.infrastructure.adapter.out.persistence.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private static final String USER_LOG_NAME = "user";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(USER_LOG_NAME);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserHelper userHelper;

    @TrackExecutionTime
    public UserModel findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @TrackExecutionTime
    public OldUserResponseDTO getUser(Principal principal) {
        var user = userHelper.getUser(principal);
        return OldUserResponseDTO.fromUser(user);
    }

    @Transactional
    @TrackExecutionTime
    public UserModel create(UserModel userDTO) throws UserAlreadyExistException {
        var user = createUser(userDTO);
        userHelper.createVerification(user);
        return user;
    }

    private UserModel createUser(UserModel inputUser) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputNewDTO(), inputUser);
        userHelper.checkUniqueUserCreateParameters(inputUser);

        var user = UserModel.builder()
                .id(UUID.randomUUID())
                .username(inputUser.username())
                .email(inputUser.email())
                .phone(inputUser.phone())
                .roles(Set.of(roleRepository.findByName("ROLE_USER").orElseThrow()))
                .password(passwordEncoder.encode(inputUser.password()))
                .build();

        userRepository.save(user);
        log.info(crudLogConstants.getSaveEntityToDatabase(), user);
        return user;
    }

    private OldUserResponseDTO convertUserToUserResponseDTO(UserEntity user) {
        var userResponseDTO = OldUserResponseDTO.fromUser(user);
        log.debug(crudLogConstants.getOutputDTOAfterMapping(), userResponseDTO);
        return userResponseDTO;
    }

    @Transactional
    @TrackExecutionTime
    public UserModel update(UserModel principal, UserModel input) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), input, principal);
        var currentUser = userRepository.getById(principal.id());
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), currentUser, input);
        var s = userHelper.checkUsername(currentUser, input.username());
        var e = userHelper.checkEmail(currentUser, input.email());
        var ty = userHelper.checkPhone(currentUser, input.phone());
        var d = userHelper.checkPassword(currentUser, input.password());

        UserModel save = UserModel.builder()
                .id(currentUser.id())
                .username(s ? input.username() : currentUser.username())
                .password(d ? passwordEncoder.encode(input.password()) : currentUser.password())
                .phone(ty ? input.phone() : currentUser.phone())
                .email(e ? input.email() : currentUser.email())
                .isPhoneConfirmed(!ty && currentUser.isPhoneConfirmed())
                .isEmailConfirmed(!e && currentUser.isEmailConfirmed())
                .roles(currentUser.roles())
                .build();

        var saved = userRepository.save(save);
        log.info(crudLogConstants.getUpdateEntityToDatabase(), saved);
        return saved;
    }

    @Transactional
    @TrackExecutionTime
    public Void delete(UserModel principal) {
        log.debug(crudLogConstants.getInputEntityForDelete(), principal);
        var currentUser = userRepository.getById(principal.id());
        log.debug(crudLogConstants.getInputEntityForDelete(), currentUser);
        userRepository.delete(currentUser);
        log.info(crudLogConstants.getDeleteEntityFromDatabase(), currentUser);
        return null;
    }

}
