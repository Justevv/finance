package com.manager.finance.helper;


import com.manager.finance.dto.UserDTO;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.entity.VerificationType;
import com.manager.finance.event.OnEmailUpdateCompleteEvent;
import com.manager.finance.event.OnPhoneUpdateCompleteEvent;
import com.manager.finance.exception.UserAlreadyExistException;
import com.manager.finance.model.VerificationModel;
import com.manager.finance.repository.UserRepository;
import com.manager.finance.service.ConfirmationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import java.security.Principal;

import static com.manager.finance.constant.Constant.USER_DOES_NOT_EXISTS;

@Service
@Slf4j
public class UserHelper {
    private static final String EMAIL_EXISTS_ERROR_MESSAGE = "There is an account with that email address: ";
    private static final String PHONE_EXISTS_ERROR_MESSAGE = "There is an account with that phone: ";
    private static final String USERNAME_EXISTS_ERROR_MESSAGE = "There is an account with that username: ";
    @Autowired
    private VerificationModel verificationModel;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private ConfirmationService confirmationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void publishCreateUserEvent(UserEntity user) {
        var verificationEmail = verificationModel.createAndSaveVerification(user, VerificationType.EMAIL);
        eventPublisher.publishEvent(new OnEmailUpdateCompleteEvent(verificationEmail));
        var verificationPhone = verificationModel.createAndSaveVerification(user, VerificationType.PHONE);
        eventPublisher.publishEvent(new OnPhoneUpdateCompleteEvent(verificationPhone));
    }

    public void checkUniqueUserCreateParameters(@Valid UserDTO userDTO) throws UserAlreadyExistException {
        if (confirmationService.isEmailAlreadyConfirmed(userDTO.getEmail())) {
            throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + userDTO.getEmail());
        }
        if (confirmationService.isPhoneAlreadyConfirmed(userDTO.getPhone())) {
            throw new UserAlreadyExistException(PHONE_EXISTS_ERROR_MESSAGE + userDTO.getPhone());
        }
        if (isUsernameExists(userDTO.getUsername())) {
            throw new UserAlreadyExistException(USERNAME_EXISTS_ERROR_MESSAGE + userDTO.getUsername());
        }
    }

    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void updatePassword(UserEntity user, String password) {
        if (password != null) {
            log.debug("The password was updated");
            user.setPassword(passwordEncoder.encode(password));
        }
    }

    public void updatePhone(UserEntity user, String phone) {
        if (phone != null && !phone.equals(user.getPhone())) {
            if (confirmationService.isPhoneAlreadyConfirmed(phone)) {
                throw new UserAlreadyExistException(PHONE_EXISTS_ERROR_MESSAGE + phone);
            }
            log.debug("The phone was updated");
            user.setPhone(phone);
            user.setPhoneConfirmed(false);
            var verification = verificationModel.createAndSaveVerification(user, VerificationType.PHONE);
            eventPublisher.publishEvent(new OnPhoneUpdateCompleteEvent(verification));
        }
    }

    public void updateEmail(UserEntity user, String email) {
        if (email != null && !email.equals(user.getEmail())) {
            if (confirmationService.isEmailAlreadyConfirmed(email)) {
                throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + email);
            }
            log.debug("The email was updated");
            user.setEmail(email);
            user.setEmailConfirmed(false);
            var verification = verificationModel.createAndSaveVerification(user, VerificationType.EMAIL);
            eventPublisher.publishEvent(new OnEmailUpdateCompleteEvent(verification));
        }
    }

    public void updateUsername(UserEntity user, String username) {
        if (username != null && !username.equals(user.getUsername())) {
            if (isUsernameExists(username)) {
                throw new UserAlreadyExistException(USERNAME_EXISTS_ERROR_MESSAGE + username);
            }
            log.debug("The username was updated");
            user.setUsername(username);
        }
    }

    public UserEntity getUser(Principal principal) {
        log.debug("input principal is {}", principal);
        var userEntity = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS));
        log.debug("Current user is {}", userEntity);
        return userEntity;
    }

}