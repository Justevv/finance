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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

@Service
@Slf4j
public class UserModel extends CrudModel<UserEntity, UserDTO> {
    private static final String CATEGORY = "user";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(CATEGORY);
    private static final String EMAIL_EXISTS_ERROR_MESSAGE = "There is an account with that email address: ";
    private static final String PHONE_EXISTS_ERROR_MESSAGE = "There is an account with that phone: ";
    private static final String USERNAME_EXISTS_ERROR_MESSAGE = "There is an account with that username: ";
    private static final String USER_DOES_NOT_EXISTS_ERROR_MESSAGE = "User doesn't exists";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUser(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS_ERROR_MESSAGE));
    }

    //        @Override
    public UserResponseDTO create(UserDTO userDTO) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputDataNew(), userDTO);
        checkUniqueAccountCreateParameters(userDTO);

        var user = getMapper().map(userDTO, UserEntity.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").orElseThrow()));

        userRepository.save(user);
        log.info(crudLogConstants.getSaveToDatabase(), user);

        return getMapper().map(user, UserResponseDTO.class);
    }

    private void checkUniqueAccountCreateParameters(UserDTO userDTO) throws UserAlreadyExistException {
        if (isEmailExists(userDTO.getEmail())) {
            throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + userDTO.getEmail());
        }
        if (isPhoneExists(userDTO.getPhone())) {
            throw new UserAlreadyExistException(PHONE_EXISTS_ERROR_MESSAGE + userDTO.getPhone());
        }
        if (isUsernameExists(userDTO.getUsername())) {
            throw new UserAlreadyExistException(USERNAME_EXISTS_ERROR_MESSAGE + userDTO.getUsername());
        }
    }

    private boolean isEmailExists(String email) {
        var user = userRepository.findByEmail(email);
        return user.isPresent() && user.get().isEmailConfirmed();
    }

    private boolean isPhoneExists(String phone) {
        var user = userRepository.findByPhone(phone);
        return user.isPresent() && user.get().isPhoneConfirmed();
    }

    private boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    //        @Override
    @Transactional
    public UserResponseDTO update(UserEntity user, UserUpdateDTO userDTO) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputDataToChange(), userDTO, user);
        getMapper().map(userDTO, user);
        if (userDTO.getEmail() != null && !user.getEmail().equals(userDTO.getEmail())) {
            user.setEmailConfirmed(false);
        }
        if (userDTO.getPhone() != null && !user.getPhone().equals(userDTO.getPhone())) {
            user.setPhoneConfirmed(false);
        }
        checkUniqueAccountUpdateParameters(user, userDTO);
        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userRepository.save(user);
        log.info(crudLogConstants.getUpdatedToDatabase(), user);
        return getMapper().map(user, UserResponseDTO.class);
    }

    private void checkUniqueAccountUpdateParameters(UserEntity user, UserUpdateDTO userDTO) throws UserAlreadyExistException {
        var email = userDTO.getEmail();
        if (!user.getEmail().equals(email) && isEmailExists(email)) {
            throw new UserAlreadyExistException(EMAIL_EXISTS_ERROR_MESSAGE + email);
        }
        var phone = userDTO.getPhone();
        if (!user.getPhone().equals(phone) && isPhoneExists(phone)) {
            throw new UserAlreadyExistException(PHONE_EXISTS_ERROR_MESSAGE + phone);
        }
        var username = userDTO.getUsername();
        if (!user.getUsername().equals(username) && isUsernameExists(username)) {
            throw new UserAlreadyExistException(USERNAME_EXISTS_ERROR_MESSAGE + username);
        }
    }

    @Override
    public CrudEntity create(UserDTO userDTO, Principal principal) {
        return null;
    }

    @Override
    public CrudEntity update(UserEntity userEntity, UserDTO userDTO) {
        return null;
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
    public Void delete(Principal principal) {
        log.debug(crudLogConstants.getInputDataForDelete(), principal);
        var user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS_ERROR_MESSAGE));
        delete(user);
        return null;
    }

    @Transactional
    public UserResponseDTO update(Principal principal, UserUpdateDTO userDTO) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputDataToChange(), userDTO, principal);
        var user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS_ERROR_MESSAGE));
        return update(user, userDTO);
    }

}