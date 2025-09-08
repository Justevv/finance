package com.manager.user.service;

import com.manager.user.dto.UserDTO;
import com.manager.user.dto.UserUpdateDTO;
import com.manager.user.dto.response.UserResponseDTO;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.exception.UserAlreadyExistException;
import com.manager.user.exception.UserNotFoundException;
import com.manager.user.helper.UserHelper;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.infrastructure.adapter.out.persistence.repository.RoleRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private static final String USER_LOG_NAME = "user";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(USER_LOG_NAME);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserHelper userHelper;

    @TrackExecutionTime
    public UserEntity findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @TrackExecutionTime
    public UserResponseDTO getUser(Principal principal) {
        var user = userHelper.getUser(principal);
        return UserResponseDTO.fromUser(user);
    }

    @Transactional
    @TrackExecutionTime
    public UserResponseDTO createAndGetDTO(UserDTO userDTO) throws UserAlreadyExistException {
        var user = create(userDTO);
        return UserResponseDTO.fromUser(user);
    }

    @TrackExecutionTime
    public UserEntity create(UserDTO userDTO) throws UserAlreadyExistException {
        var user = createUser(userDTO);
        userHelper.createVerification(user);
        return user;
    }

    @TrackExecutionTime
    private UserEntity createUser(UserDTO userDTO) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputNewDTO(), userDTO);
        userHelper.checkUniqueUserCreateParameters(userDTO);

        var user = UserEntity.builder()
                .id(UUID.randomUUID())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .roles(Set.of(roleRepository.findByName("ROLE_USER").orElseThrow()))
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        userRepository.save(user);
        log.info(crudLogConstants.getSaveEntityToDatabase(), user);
        return user;
    }

    private UserResponseDTO convertUserToUserResponseDTO(UserEntity user) {
        var userResponseDTO = UserResponseDTO.fromUser(user);
        log.debug(crudLogConstants.getOutputDTOAfterMapping(), userResponseDTO);
        return userResponseDTO;
    }

    @Transactional
    @TrackExecutionTime
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
    @TrackExecutionTime
    public Void delete(Principal principal) {
        log.debug(crudLogConstants.getInputEntityForDelete(), principal);
        var user = userHelper.getUser(principal);
        log.debug(crudLogConstants.getInputEntityForDelete(), user);
        userRepository.delete(user);
        log.info(crudLogConstants.getDeleteEntityFromDatabase(), user);
        return null;
    }

}
