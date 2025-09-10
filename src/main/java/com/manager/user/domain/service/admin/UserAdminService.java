package com.manager.user.domain.service.admin;

import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserAdminDTO;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.UserAdminResponseDTO;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserAdminUpdateDTO;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.exception.UserAlreadyExistException;
import com.manager.user.helper.UserHelper;
import com.manager.finance.log.CrudLogConstants;
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
    private final UserSpringDataRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneVerificationService phoneVerificationService;
    private final EmailVerificationService emailVerificationService;
    private final UserHelper userHelper;

    public List<UserAdminResponseDTO> getAll() {
        var userEntities = userRepository.findAll();
        return userEntities.stream().map(this::convertUserToUserResponseDTO).toList();
    }

    public UserAdminResponseDTO get(UserEntity user) {
        return convertUserToUserResponseDTO(user);
    }

    @Transactional
    public UserAdminResponseDTO createAndGetDTO(UserAdminDTO userAdminDTO) {
        var user = createUser(userAdminDTO);
        userHelper.createVerification(user);
        return convertUserToUserResponseDTO(user);
    }

    @Transactional
    public UserAdminResponseDTO update(UserEntity user, UserAdminUpdateDTO userUpdateDTO) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), user, userUpdateDTO);
        userHelper.updateUsername(user, userUpdateDTO.getUsername());
        userHelper.updateEmail(user, userUpdateDTO.getEmail());
        userHelper.updatePhone(user, userUpdateDTO.getPhone());
        userHelper.updatePassword(user, userUpdateDTO.getPassword());
        updateEmailConfirmed(user, userUpdateDTO.isEmailConfirmed());
        updatePhoneConfirmed(user, userUpdateDTO.isPhoneConfirmed());
        userRepository.save(user);
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
        userRepository.delete(user);
        log.info(crudLogConstants.getDeleteEntityFromDatabase(), user);
        return null;
    }

    private UserEntity createUser(UserAdminDTO userAdminDTO) {
        log.debug(crudLogConstants.getInputNewDTO(), userAdminDTO);
        userHelper.checkUniqueUserCreateParameters(userAdminDTO);

        var user = getMapper().map(userAdminDTO, UserEntity.class);
        user.setId(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(userAdminDTO.getPassword()));

        userRepository.save(user);
        log.info(crudLogConstants.getSaveEntityToDatabase(), user);
        return user;
    }

    private UserAdminResponseDTO convertUserToUserResponseDTO(UserEntity user) {
        var userResponseDTO = getMapper().map(user, UserAdminResponseDTO.class);
        log.debug(crudLogConstants.getOutputDTOAfterMapping(), userResponseDTO);
        return userResponseDTO;
    }

}

