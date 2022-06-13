package com.manager.finance.model;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.UserDTO;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.exception.UserAlreadyExistException;
import com.manager.finance.repository.RoleRepository;
import com.manager.finance.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserModel {
    private static final String CATEGORY = "user";
    private final LogConstants logConstants = new LogConstants(CATEGORY);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserEntity create(UserDTO userDTO) throws UserAlreadyExistException {
        log.debug(logConstants.getInputDataNew(), userDTO);
        checkUniqueAccountCreateParameters(userDTO);

        ModelMapper modelMapper = new ModelMapper();
        UserEntity user = modelMapper.map(userDTO, UserEntity.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//        user.setRoles(List.of(roleRepo.findByName("ROLE_USER").orElseThrow()));

        userRepository.save(user);
        log.info(logConstants.getSaveToDatabase(), user);
        return user;
    }

    private void checkUniqueAccountCreateParameters(UserDTO userDTO) throws UserAlreadyExistException {
        if (isEmailExists(userDTO.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + userDTO.getEmail());
        }
        if (isPhoneExists(userDTO.getPhone())) {
            throw new UserAlreadyExistException("There is an account with that phone: "
                    + userDTO.getPhone());
        }
        if (isUsernameExists(userDTO.getUsername())) {
            throw new UserAlreadyExistException("There is an account with that username: "
                    + userDTO.getUsername());
        }
    }

    private boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private boolean isPhoneExists(String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }

    private boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserEntity update(UserEntity user, UserDTO userDTO) throws UserAlreadyExistException {
        log.debug(logConstants.getInputDataToChange(), user, userDTO);
        checkUniqueAccountUpdateParameters(user, userDTO);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(userDTO, user);
        userRepository.save(user);
        log.info(logConstants.getUpdatedToDatabase(), user);
        return user;
    }

    private void checkUniqueAccountUpdateParameters(UserEntity user, UserDTO userDTO) throws UserAlreadyExistException {
        var email = userDTO.getEmail();
        if (!user.getEmail().equals(email) && isEmailExists(email)) {
            throw new UserAlreadyExistException("There is an account with that email address: " + email);
        }
        var phone = userDTO.getPhone();
        if (!user.getPhone().equals(phone) && isPhoneExists(phone)) {
            throw new UserAlreadyExistException("There is an account with that phone: " + phone);
        }
        var username = userDTO.getUsername();
        if (!user.getUsername().equals(username) && isUsernameExists(username)) {
            throw new UserAlreadyExistException("There is an account with that username: " + username);
        }
    }

    public Void delete(UserEntity user) {
        log.debug(logConstants.getInputDataForDelete(), user);
        userRepository.delete(user);
        log.info(logConstants.getDeletedFromDatabase(), user);
        return null;
    }

//    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
//        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + email));
//        if (user == null) {
//            throw new UsernameNotFoundException("No user found with username: " + email);
//        }
//        boolean enabled = true;
//        boolean accountNonExpired = true;
//        boolean credentialsNonExpired = true;
//        boolean accountNonLocked = true;
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(), user.getPassword().toLowerCase(), enabled, accountNonExpired,
//                credentialsNonExpired, accountNonLocked, getAuthorities(user.getRoles()));
//    }
//
//    private static List<GrantedAuthority> getAuthorities(List<String> roles) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        for (String role : roles) {
//            authorities.add(new SimpleGrantedAuthority(role));
//        }
//        return authorities;
//    }

    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

}

