package com.manager.user.helper;


import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.domain.model.UserModel;
import com.manager.user.domain.service.verification.EmailVerificationService;
import com.manager.user.domain.service.verification.PhoneVerificationService;
import com.manager.user.domain.exception.UserAlreadyExistException;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserDTO;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.infrastructure.adapter.out.persistence.mapper.EntityMapper;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.UserSpringDataRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

import static com.manager.finance.constant.Constant.USER_DOES_NOT_EXISTS;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserHelper {
    private static final String EMAIL_EXISTS_ERROR_MESSAGE = "There is an account with that email address: ";
    private static final String PHONE_EXISTS_ERROR_MESSAGE = "There is an account with that phone: ";
    private static final String USERNAME_EXISTS_ERROR_MESSAGE = "There is an account with that username: ";
    private final EmailVerificationService emailVerificationService;
    private final PhoneVerificationService phoneVerificationService;
    private final UserSpringDataRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper<UserEntity, UserModel> userMapper;

    @TrackExecutionTime
    public void createVerification(UserEntity user) {
        emailVerificationService.createAndSaveVerification(user);
        phoneVerificationService.createAndSaveVerification(user);
    }

    @TrackExecutionTime
    public void createVerification(UserModel user) {
        emailVerificationService.createAndSaveVerification(user);
        phoneVerificationService.createAndSaveVerification(user);
    }

    @TrackExecutionTime
    public void checkUniqueUserCreateParameters(@Valid UserDTO userDTO) throws UserAlreadyExistException {
        if (emailVerificationService.isEmailAlreadyConfirmed(userDTO.getEmail())) {
            throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + userDTO.getEmail());
        }
        if (phoneVerificationService.isPhoneAlreadyConfirmed(userDTO.getPhone())) {
            throw new UserAlreadyExistException(PHONE_EXISTS_ERROR_MESSAGE + userDTO.getPhone());
        }
        if (isUsernameExists(userDTO.getUsername())) {
            throw new UserAlreadyExistException(USERNAME_EXISTS_ERROR_MESSAGE + userDTO.getUsername());
        }
    }


    @TrackExecutionTime
    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @TrackExecutionTime
    public void updatePassword(UserEntity user, String password) {
        if (password != null) {
            log.debug("The password was updated");
            user.setPassword(passwordEncoder.encode(password));
        }
    }

    @TrackExecutionTime
    public void updatePhone(UserEntity user, String phone) {
        if (phone != null && !phone.equals(user.getPhone())) {
            if (phoneVerificationService.isPhoneAlreadyConfirmed(phone)) {
                throw new UserAlreadyExistException(PHONE_EXISTS_ERROR_MESSAGE + phone);
            }
            log.debug("The phone was updated");
            user.setPhone(phone);
            user.setPhoneConfirmed(false);
            phoneVerificationService.createAndSaveVerification(userMapper.toModel(user));
        }
    }

    @TrackExecutionTime
    public void updateEmail(UserEntity user, String email) {
        if (email != null && !email.equals(user.getEmail())) {
            if (emailVerificationService.isEmailAlreadyConfirmed(email)) {
                throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + email);
            }
            log.debug("The email was updated");
            user.setEmail(email);
            user.setEmailConfirmed(false);
            emailVerificationService.createAndSaveVerification(userMapper.toModel(user));
        }
    }

    @TrackExecutionTime
    public void updateUsername(UserEntity user, String username) {
        if (username != null && !username.equals(user.getUsername())) {
            if (isUsernameExists(username)) {
                throw new UserAlreadyExistException(USERNAME_EXISTS_ERROR_MESSAGE + username);
            }
            log.debug("The username was updated");
            user.setUsername(username);
        }
    }


}