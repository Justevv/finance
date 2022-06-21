package com.manager.finance.model;

import com.manager.finance.config.CrudLogConstants;
import com.manager.finance.dto.UserDTO;
import com.manager.finance.dto.UserResponseDTO;
import com.manager.finance.dto.UserUpdateDTO;
import com.manager.finance.entity.CrudEntity;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.exception.UserAlreadyExistException;
import com.manager.finance.repository.RoleRepository;
import com.manager.finance.repository.UserRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

import static com.manager.finance.Constant.USER_DOES_NOT_EXISTS;

@Service
@Slf4j
public class UserModel extends CrudModel<UserEntity, UserDTO> {
    private static final String CATEGORY = "user";
    private static final String EMAIL_EXISTS_ERROR_MESSAGE = "There is an account with that email address: ";
    private static final String PHONE_EXISTS_ERROR_MESSAGE = "There is an account with that phone: ";
    private static final String USERNAME_EXISTS_ERROR_MESSAGE = "There is an account with that username: ";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(CATEGORY);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Getter
    private final ModelMapper mapper = new ModelMapper();


    public List<UserEntity> getUsersAllInfo() {
        return userRepository.findAll();
    }

    public List<UserResponseDTO> getUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream().map(x -> getMapper().map(x, UserResponseDTO.class)).toList();
    }

    public UserResponseDTO getUser(Principal principal) {
        UserEntity user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS));
        return getMapper().map(user, UserResponseDTO.class);
    }

    @Transactional
    public UserResponseDTO create(UserDTO userDTO) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputDataNew(), userDTO);
        checkUniqueAccountCreateParameters(userDTO);

        var user = getMapper().map(userDTO, UserEntity.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").orElseThrow()));

        userRepository.save(user);
        log.info(crudLogConstants.getSaveToDatabase(), user);
//        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
        return getMapper().map(user, UserResponseDTO.class);
    }

    private void checkUniqueAccountCreateParameters(UserDTO userDTO) throws UserAlreadyExistException {
        if (isEmailConfirmed(userDTO.getEmail())) {
            throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + userDTO.getEmail());
        }
        if (isPhoneConfirmed(userDTO.getPhone())) {
            throw new UserAlreadyExistException(PHONE_EXISTS_ERROR_MESSAGE + userDTO.getPhone());
        }
        if (isUsernameExists(userDTO.getUsername())) {
            throw new UserAlreadyExistException(USERNAME_EXISTS_ERROR_MESSAGE + userDTO.getUsername());
        }
    }

    private boolean isEmailConfirmed(String email) {
        var user = userRepository.findByEmail(email);
        return user.stream().anyMatch(UserEntity::isEmailConfirmed);
    }

    private boolean isPhoneConfirmed(String phone) {
        var user = userRepository.findByPhone(phone);
        return user.stream().anyMatch(UserEntity::isPhoneConfirmed);
    }

    private boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public UserResponseDTO update(UserEntity user, UserUpdateDTO userDTO) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputDataToChange(), user, userDTO);
        checkUniqueAccountUpdateParameters(user, userDTO);
        if (userDTO.getUsername() != null && !user.getUsername().equals(userDTO.getUsername())) {
            log.debug("The username was updated");
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getEmail() != null && !user.getEmail().equals(userDTO.getEmail())) {
            log.debug("The email was updated");
            user.setEmail(userDTO.getEmail());
            user.setEmailConfirmed(false);
//            eventPublisher.publishEvent(new OnEmailUpdateCompleteEvent(user));
        }
        if (userDTO.getPhone() != null && !user.getPhone().equals(userDTO.getPhone())) {
            log.debug("The phone was updated");
            user.setPhone(userDTO.getPhone());
            user.setPhoneConfirmed(false);
//            eventPublisher.publishEvent(new OnPhoneUpdateCompleteEvent(user));
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userRepository.save(user);
        log.info(crudLogConstants.getUpdatedToDatabase(), user);
        return getMapper().map(user, UserResponseDTO.class);
    }

    private void checkUniqueAccountUpdateParameters(UserEntity user, UserUpdateDTO userDTO) throws UserAlreadyExistException {
        var email = userDTO.getEmail();
        if (!user.getEmail().equals(email) && isEmailConfirmed(email)) {
            throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + email);
        }
        var phone = userDTO.getPhone();
        if (!user.getPhone().equals(phone) && isPhoneConfirmed(phone)) {
            throw new UserAlreadyExistException(PHONE_EXISTS_ERROR_MESSAGE + phone);
        }
        var username = userDTO.getUsername();
        if (!user.getUsername().equals(username) && isUsernameExists(username)) {
            throw new UserAlreadyExistException(USERNAME_EXISTS_ERROR_MESSAGE + username);
        }
    }

    @Override
    public List<UserEntity> getAll(Principal principal) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CrudEntity create(UserDTO userDTO, Principal principal) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CrudEntity update(UserEntity userEntity, UserDTO userDTO) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional
    public Void delete(UserEntity user) {
        log.debug(crudLogConstants.getInputDataForDelete(), user);
        userRepository.delete(user);
        log.info(crudLogConstants.getDeletedFromDatabase(), user);
        return null;
    }

    @Transactional
    public UserResponseDTO update(Principal principal, UserUpdateDTO userDTO) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputDataToChange(), userDTO, principal);
        var user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS));
        return update(user, userDTO);
    }

    @Transactional
    public Void delete(Principal principal) {
        log.debug(crudLogConstants.getInputDataForDelete(), principal);
        var user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS));
        delete(user);
        return null;
    }


}
