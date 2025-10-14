package com.manager.user.domain.service.admin;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.application.port.out.repository.UserRepository;
import com.manager.user.domain.model.UserModel;
import com.manager.user.domain.service.UserMainService;
import com.manager.user.domain.service.UserService;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.UserAdminResponseDTOOld;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserAdminUpdateDTO;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.domain.exception.UserAlreadyExistException;
import com.manager.user.helper.UserHelper;
import com.manager.finance.log.CrudLogConstants;
import com.manager.user.infrastructure.adapter.out.persistence.repository.RoleRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.UserSpringDataRepository;
import com.manager.user.domain.service.verification.EmailVerificationService;
import com.manager.user.domain.service.verification.PhoneVerificationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAdminService {
    private static final String EMAIL_EXISTS_ERROR_MESSAGE = "There is an account with that email address: ";
    private static final String PHONE_EXISTS_ERROR_MESSAGE = "There is an account with that phone: ";
    private static final String USER_LOG_NAME = "adminUser";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(USER_LOG_NAME);
    @Getter
    private final ModelMapper mapper;
    private final UserSpringDataRepository userSpringDataRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneVerificationService phoneVerificationService;
    private final EmailVerificationService emailVerificationService;
    private final UserHelper userHelper;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final UserMainService userMainService;

    public List<UserAdminResponseDTOOld> getAll() {
        var userEntities = userSpringDataRepository.findAll();
        return userEntities.stream().map(this::convertUserToUserResponseDTO).toList();
    }

    public UserAdminResponseDTOOld get(UserEntity user) {
        return convertUserToUserResponseDTO(user);
    }

    @Transactional
    public UserModel createAndGetDTO(UserModel userAdminDTO) {
        var user = createUser(userAdminDTO);
        userHelper.createVerification(user);
        return user;
    }

    @Transactional
    @TrackExecutionTime
    public UserModel update(UUID uuid, UserModel input) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), input, uuid);
        var currentUser = userRepository.getById(uuid);
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), currentUser, input);
        var updateUsername = userMainService.checkUsername(currentUser, input.username());
        var updateEmail = userMainService.checkEmail(currentUser, input.email());
        var updatePhone = userMainService.checkPhone(currentUser, input.phone());
        var updatePassword = userMainService.checkPassword(currentUser, input.password());

        UserModel save = UserModel.builder()
                .id(currentUser.id())
                .username(updateUsername ? input.username() : currentUser.username())
                .password(updatePassword ? passwordEncoder.encode(input.password()) : currentUser.password())
                .phone(updatePhone ? input.phone() : currentUser.phone())
                .email(updateEmail ? input.email() : currentUser.email())
                .isPhoneConfirmed(input.isPhoneConfirmed())
                .isEmailConfirmed(input.isEmailConfirmed())
                .roles(currentUser.roles())
                .build();

        var saved = userRepository.save(save);
        log.info(crudLogConstants.getUpdateEntityToDatabase(), saved);
        return saved;
    }

    private void updatePhoneConfirmed(UserEntity user, boolean isPhoneConfirmed) {
        if (isPhoneConfirmed != user.isPhoneConfirmed()) {
            log.debug("The PhoneConfirmed was updated");
            if (isPhoneConfirmed) {
                if (phoneVerificationService.isPhoneAlreadyConfirmed(user.getPhone())) {
                    throw new UserAlreadyExistException(PHONE_EXISTS_ERROR_MESSAGE + user.getPhone());
                }
                user.setPhoneConfirmed(true);
            }
            user.setPhoneConfirmed(isPhoneConfirmed);
        }
    }

    private void updateEmailConfirmed(UserEntity user, boolean isEmailConfirmed) {
        if (isEmailConfirmed != user.isEmailConfirmed()) {
            log.debug("The EmailConfirmed was updated");
            if (isEmailConfirmed) {
                if (emailVerificationService.isEmailAlreadyConfirmed(user.getEmail())) {
                    throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + user.getEmail());
                }
                user.setEmailConfirmed(true);
            }
            user.setEmailConfirmed(isEmailConfirmed);
        }
    }


    @Transactional
    @TrackExecutionTime
    public void delete(UUID userId) {
        var currentUser = userRepository.getById(userId);
        userMainService.delete(currentUser);
    }

    private UserModel createUser(UserModel inputUser) {
        log.debug(crudLogConstants.getInputNewDTO(), inputUser);
        userService.checkUniqueUserCreateParameters(inputUser);

        var user = UserModel.builder()
                .id(UUID.randomUUID())
                .username(inputUser.username())
                .email(inputUser.email())
                .phone(inputUser.phone())
                .roles(Set.of(roleRepository.findByName("ROLE_USER").orElseThrow()))
                .password(passwordEncoder.encode(inputUser.password()))
                .isPhoneConfirmed(inputUser.isPhoneConfirmed())
                .isEmailConfirmed(inputUser.isEmailConfirmed())
                .build();

        userRepository.save(user);
        log.info(crudLogConstants.getSaveEntityToDatabase(), user);
        return user;
    }

    private UserAdminResponseDTOOld convertUserToUserResponseDTO(UserEntity user) {
        var userResponseDTO = getMapper().map(user, UserAdminResponseDTOOld.class);
        log.debug(crudLogConstants.getOutputDTOAfterMapping(), userResponseDTO);
        return userResponseDTO;
    }

}

