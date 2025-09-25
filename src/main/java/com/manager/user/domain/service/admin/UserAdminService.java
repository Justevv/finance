package com.manager.user.domain.service.admin;

import com.manager.user.application.port.out.repository.UserRepository;
import com.manager.user.domain.model.UserModel;
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
    public UserAdminResponseDTOOld update(UserEntity user, UserAdminUpdateDTO userUpdateDTO) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), user, userUpdateDTO);
        userHelper.updateUsername(user, userUpdateDTO.getUsername());
        userHelper.updateEmail(user, userUpdateDTO.getEmail());
        userHelper.updatePhone(user, userUpdateDTO.getPhone());
        userHelper.updatePassword(user, userUpdateDTO.getPassword());
        updateEmailConfirmed(user, userUpdateDTO.isEmailConfirmed());
        updatePhoneConfirmed(user, userUpdateDTO.isPhoneConfirmed());
        userSpringDataRepository.save(user);
        log.info(crudLogConstants.getUpdateEntityToDatabase(), user);
        var adminUserResponseDTO = convertUserToUserResponseDTO(user);
        log.debug(crudLogConstants.getOutputDTOAfterMapping(), adminUserResponseDTO);
        return adminUserResponseDTO;
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
    public Void delete(UserEntity user) {
        log.debug(crudLogConstants.getInputEntityForDelete(), user);
        userSpringDataRepository.delete(user);
        log.info(crudLogConstants.getDeleteEntityFromDatabase(), user);
        return null;
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

