package com.manager.user.domain.service;

import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.application.port.out.repository.UserRepository;
import com.manager.user.domain.exception.UserAlreadyExistException;
import com.manager.user.domain.model.UserModel;
import com.manager.user.domain.service.verification.EmailVerificationService;
import com.manager.user.domain.service.verification.PhoneVerificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserMainService {
    private static final String EMAIL_EXISTS_ERROR_MESSAGE = "There is an account with that email address: ";
    private static final String PHONE_EXISTS_ERROR_MESSAGE = "There is an account with that phone: ";
    private static final String USERNAME_EXISTS_ERROR_MESSAGE = "There is an account with that username: ";
    private static final String USER_LOG_NAME = "user";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(USER_LOG_NAME);
    private final UserRepository userRepository;
    private final PhoneVerificationService phoneVerificationService;
    private final EmailVerificationService emailVerificationService;

    @Transactional
    @TrackExecutionTime
    public void delete(UserModel user) {
        log.debug(crudLogConstants.getInputEntityForDelete(), user);
        userRepository.delete(user);
        log.info(crudLogConstants.getDeleteEntityFromDatabase(), user);
    }

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

    private boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean checkPassword(UserModel user, String password) {
        return password != null;
    }

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

    public boolean checkEmail(UserModel user, String newEmail) {
        if (newEmail == null || newEmail.equals(user.email())) {
            return false;
        }
        if (isEmailAlreadyConfirmed(newEmail)) {
            throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + newEmail);
        }
        log.debug("The email can be updated");
        return true;
    }

    private boolean isEmailAlreadyConfirmed(String email) {
        var emailConfirmed = userRepository.findByEmailAndIsEmailConfirmed(email, true);
        var isEmailAlreadyConfirmed = emailConfirmed.isPresent();
        log.debug("Is email {} already confirmed: {}", email, isEmailAlreadyConfirmed);
        return isEmailAlreadyConfirmed;
    }
}
