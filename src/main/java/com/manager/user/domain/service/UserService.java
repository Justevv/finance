package com.manager.user.domain.service;

import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.application.port.in.UserUseCase;
import com.manager.user.application.port.out.repository.UserRepository;
import com.manager.user.domain.exception.UserAlreadyExistException;
import com.manager.user.domain.model.UserModel;
import com.manager.user.domain.service.verification.PhoneVerificationService;
import com.manager.user.domain.service.verification.VerificationService;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.RoleSpringDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserUseCase {
    private static final String EMAIL_EXISTS_ERROR_MESSAGE = "There is an account with that email address: ";
    private static final String PHONE_EXISTS_ERROR_MESSAGE = "There is an account with that phone: ";
    private static final String USERNAME_EXISTS_ERROR_MESSAGE = "There is an account with that username: ";
    private static final String USER_LOG_NAME = "user";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(USER_LOG_NAME);
    private final UserRepository userRepository;
    private final RoleSpringDataRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;
    private final PhoneVerificationService phoneVerificationService;
    private final UserMainService userMainService;

    @TrackExecutionTime
    @Override
    public UserModel getById(UUID id) {
        return userRepository.getById(id);
    }

    @Transactional
    @TrackExecutionTime
    @Override
    public UserModel create(UserModel inputUser) throws UserAlreadyExistException {
        var user = createUser(inputUser);
        verificationService.createVerification(user);
        return user;
    }

    private UserModel createUser(UserModel inputUser) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputNewDTO(), inputUser);
        checkUniqueUserCreateParameters(inputUser);

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

    @Transactional
    @TrackExecutionTime
    @Override
    public UserModel update(UserModel principal, UserModel input) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), input, principal);
        var currentUser = userRepository.getById(principal.id());
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), currentUser, input);
        var updateUsername = checkUsername(currentUser, input.username());
        var updateEmail = checkEmail(currentUser, input.email());
        var updatePhone = checkPhone(currentUser, input.phone());
        var updatePassword = checkPassword(currentUser, input.password());

        UserModel save = UserModel.builder()
                .id(currentUser.id())
                .username(updateUsername ? input.username() : currentUser.username())
                .password(updatePassword ? passwordEncoder.encode(input.password()) : currentUser.password())
                .phone(updatePhone ? input.phone() : currentUser.phone())
                .email(updateEmail ? input.email() : currentUser.email())
                .isPhoneConfirmed(!updatePhone && currentUser.isPhoneConfirmed())
                .isEmailConfirmed(!updateEmail && currentUser.isEmailConfirmed())
                .roles(currentUser.roles())
                .build();

        var saved = userRepository.save(save);
        log.info(crudLogConstants.getUpdateEntityToDatabase(), saved);
        return saved;
    }

    @Transactional
    @TrackExecutionTime
    public void updatePassword(UserModel principal, String password) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), principal);
        var currentUser = userRepository.getById(principal.id());
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), currentUser);
        var updatePassword = checkPassword(currentUser, password);

        UserModel save = UserModel.builder()
                .id(currentUser.id())
                .username(currentUser.username())
                .password(updatePassword ? passwordEncoder.encode(password) : currentUser.password())
                .phone(currentUser.phone())
                .email(currentUser.email())
                .isPhoneConfirmed(currentUser.isPhoneConfirmed())
                .isEmailConfirmed(currentUser.isEmailConfirmed())
                .roles(currentUser.roles())
                .build();

        var saved = userRepository.save(save);
        log.info(crudLogConstants.getUpdateEntityToDatabase(), saved);
    }

    @Transactional
    @TrackExecutionTime
    @Override
    public void delete(UserModel user) {
        userMainService.delete(user);
    }


    @TrackExecutionTime
    public void checkUniqueUserCreateParameters(UserModel userDTO) throws UserAlreadyExistException {
        if (isEmailAlreadyConfirmed(userDTO.email())) {
            throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + userDTO.email());
        }
        if (phoneVerificationService.isPhoneAlreadyConfirmed(userDTO.phone())) {
            throw new UserAlreadyExistException(PHONE_EXISTS_ERROR_MESSAGE + userDTO.phone());
        }
        if (isUsernameExists(userDTO.username())) {
            throw new UserAlreadyExistException(USERNAME_EXISTS_ERROR_MESSAGE + userDTO.username());
        }
    }

    private boolean checkUsername(UserModel user, String username) {
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

    private boolean checkPassword(UserModel user, String password) {
        return password != null;
    }

    private boolean checkPhone(UserModel user, String newPhone) {

        if (newPhone == null || newPhone.equals(user.phone())) {
            return false;
        }
        if (phoneVerificationService.isPhoneAlreadyConfirmed(newPhone)) {
            throw new UserAlreadyExistException(PHONE_EXISTS_ERROR_MESSAGE + newPhone);
        }
        log.debug("The phone was updated");
        return true;
    }

    private boolean checkEmail(UserModel user, String newEmail) {
        if (newEmail == null || newEmail.equals(user.email())) {
            return false;
        }
        if (isEmailAlreadyConfirmed(newEmail)) {
            throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + newEmail);
        }
        log.debug("The email can be updated");
        return true;
    }

    @TrackExecutionTime
    public boolean isEmailAlreadyConfirmed(String email) {
        var emailConfirmed = userRepository.findByEmailAndIsEmailConfirmed(email, true);
        var isEmailAlreadyConfirmed = emailConfirmed.isPresent();
        log.debug("Is email {} already confirmed: {}", email, isEmailAlreadyConfirmed);
        return isEmailAlreadyConfirmed;
    }
}
