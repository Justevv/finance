package com.manager.finance.model;

import com.manager.finance.dto.UserDTO;
import com.manager.finance.dto.response.UserResponseDTO;
import com.manager.finance.dto.UserUpdateDTO;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.exception.UserAlreadyExistException;
import com.manager.finance.helper.UserHelper;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.repository.RoleRepository;
import com.manager.finance.repository.UserRepository;
import com.manager.finance.service.ConfirmationService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Set;

@Service
@Slf4j
public class UserModel {
    private static final String USER_LOG_NAME = "user";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(USER_LOG_NAME);
    @Getter
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserHelper userHelper;

    public UserResponseDTO getUser(Principal principal) {
        var user = userHelper.getUser(principal);
        return getMapper().map(user, UserResponseDTO.class);
    }

    @Transactional
    public UserResponseDTO createAndGetDTO(UserDTO userDTO) throws UserAlreadyExistException {
        var user = create(userDTO);
        return convertUserToUserResponseDTO(user);
    }

    @Transactional
    public UserEntity create(UserDTO userDTO) throws UserAlreadyExistException {
        var user = createUser(userDTO);
        userHelper.publishCreateUserEvent(user);
        return user;
    }

    private UserEntity createUser(UserDTO userDTO) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputNewDTO(), userDTO);
        userHelper.checkUniqueUserCreateParameters(userDTO);

        var user = getMapper().map(userDTO, UserEntity.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(Set.of(roleRepository.findByName("ROLE_USER").orElseThrow()));

        userRepository.save(user);
        log.info(crudLogConstants.getSaveEntityToDatabase(), user);
        return user;
    }

    private UserResponseDTO convertUserToUserResponseDTO(UserEntity user) {
        var userResponseDTO = getMapper().map(user, UserResponseDTO.class);
        log.debug(crudLogConstants.getOutputDTOAfterMapping(), userResponseDTO);
        return userResponseDTO;
    }

    @Transactional
    public UserResponseDTO update(Principal principal, UserUpdateDTO userUpdateDTO) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), userUpdateDTO, principal);
        var user = userHelper.getUser(principal);
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), user, userUpdateDTO);
        userHelper.updateUsername(user, userUpdateDTO.getUsername());
        userHelper.updateEmail(user, userUpdateDTO.getEmail());
        userHelper.updatePhone(user, userUpdateDTO.getPhone());
        userHelper.updatePassword(user, userUpdateDTO.getPassword());
        userRepository.save(user);
        log.info(crudLogConstants.getUpdateEntityToDatabase(), user);
        var adminUserResponseDTO = convertUserToUserResponseDTO(user);
        log.debug(crudLogConstants.getOutputDTOAfterMapping(), adminUserResponseDTO);
        return adminUserResponseDTO;
    }

    @Transactional
    public Void delete(Principal principal) {
        log.debug(crudLogConstants.getInputEntityForDelete(), principal);
        var user = userHelper.getUser(principal);
        log.debug(crudLogConstants.getInputEntityForDelete(), user);
        userRepository.delete(user);
        log.info(crudLogConstants.getDeleteEntityFromDatabase(), user);
        return null;
    }

}
