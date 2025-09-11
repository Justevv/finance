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
    public void checkUniqueUserCreateParameters(UserModel userDTO) throws UserAlreadyExistException {
        if (emailVerificationService.isEmailAlreadyConfirmed(userDTO.email())) {
            throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + userDTO.email());
        }
        if (phoneVerificationService.isPhoneAlreadyConfirmed(userDTO.phone())) {
            throw new UserAlreadyExistException(PHONE_EXISTS_ERROR_MESSAGE + userDTO.phone());
        }
        if (isUsernameExists(userDTO.username())) {
            throw new UserAlreadyExistException(USERNAME_EXISTS_ERROR_MESSAGE + userDTO.username());
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
    public boolean checkPassword(UserModel user, String password) {
        return password != null;
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
    public boolean checkPhone(UserModel user, String newPhone) {

        if (newPhone == null || newPhone.equals(user.phone())) {
            return false;
        }
        if (phoneVerificationService.isPhoneAlreadyConfirmed(newPhone)) {
            throw new UserAlreadyExistException(PHONE_EXISTS_ERROR_MESSAGE + newPhone);
        }
        log.debug("The phone was updated");
        return true;
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
    public boolean checkEmail(UserModel user, String newEmail) {
        if (newEmail == null || newEmail.equals(user.email())) {
            return false;
        }
        if (emailVerificationService.isEmailAlreadyConfirmed(newEmail)) {
            throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + newEmail);
        }
        log.debug("The email can be updated");
        return true;
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

    @TrackExecutionTime
    public boolean checkUsername(UserModel user, String username) {
        if (username == null || username.equals(user.username())) {
            return false;
        }
        if (isUsernameExists(username)) {
            throw new UserAlreadyExistException(USERNAME_EXISTS_ERROR_MESSAGE + username);
        }
        log.debug("The username can be updated");
        return true;
    }

    @TrackExecutionTime
    public UserEntity getUser(Principal principal) {
        log.debug("input principal is {}", principal);
        if (principal instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
            var user = usernamePasswordAuthenticationToken.getPrincipal();
            if (user instanceof UserEntity userEntity) {
                log.debug("Current user is {}", userEntity);
                return userEntity;
            }
        }
        var userEntity = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS));
        log.debug("Current user is {}", userEntity);
        return userEntity;
    }

    @TrackExecutionTime
    public UserEntity toEntity(UserModel userModel) {
        log.debug("input userModel is {}", userModel);
        return UserEntity.builder()
                .id(userModel.id())
                .username(userModel.username())
                .build();
    }

    @TrackExecutionTime
    public UserModel toModel(UserEntity userEntity) {
        log.debug("input userEntity is {}", userEntity);
        return UserModel.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .build();
    }

}