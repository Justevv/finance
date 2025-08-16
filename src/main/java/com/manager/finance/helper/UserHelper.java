package com.manager.finance.helper;


import com.manager.finance.dto.UserDTO;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.entity.VerificationType;
import com.manager.finance.event.OnEmailUpdateCompleteEvent;
import com.manager.finance.event.OnPhoneUpdateCompleteEvent;
import com.manager.finance.exception.UserAlreadyExistException;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.service.VerificationService;
import com.manager.finance.repository.UserRepository;
import com.manager.finance.service.ConfirmationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final VerificationService verificationService;
    private final ApplicationEventPublisher eventPublisher;
    private final ConfirmationService confirmationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @TrackExecutionTime
    public void publishCreateUserEvent(UserEntity user) {
        var verificationEmail = verificationService.createAndSaveVerification(user, VerificationType.EMAIL);
        eventPublisher.publishEvent(new OnEmailUpdateCompleteEvent(verificationEmail));
        var verificationPhone = verificationService.createAndSaveVerification(user, VerificationType.PHONE);
        eventPublisher.publishEvent(new OnPhoneUpdateCompleteEvent(verificationPhone));
    }

    @TrackExecutionTime
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
            if (confirmationService.isPhoneAlreadyConfirmed(phone)) {
                throw new UserAlreadyExistException(PHONE_EXISTS_ERROR_MESSAGE + phone);
            }
            log.debug("The phone was updated");
            user.setPhone(phone);
            user.setPhoneConfirmed(false);
            var verification = verificationService.createAndSaveVerification(user, VerificationType.PHONE);
            eventPublisher.publishEvent(new OnPhoneUpdateCompleteEvent(verification));
        }
    }

    @TrackExecutionTime
    public void updateEmail(UserEntity user, String email) {
        if (email != null && !email.equals(user.getEmail())) {
            if (confirmationService.isEmailAlreadyConfirmed(email)) {
                throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + email);
            }
            log.debug("The email was updated");
            user.setEmail(email);
            user.setEmailConfirmed(false);
            var verification = verificationService.createAndSaveVerification(user, VerificationType.EMAIL);
            eventPublisher.publishEvent(new OnEmailUpdateCompleteEvent(verification));
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

    @TrackExecutionTime
    public UserEntity getUser(Principal principal) {
        log.debug("input principal is {}", principal);
        var userEntity = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS));
        log.debug("Current user is {}", userEntity);
        return userEntity;
    }

}