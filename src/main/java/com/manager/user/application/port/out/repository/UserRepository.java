package com.manager.user.application.port.out.repository;

import com.manager.user.domain.model.UserModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<UserModel> findByUsername(String username);

    List<UserModel> findByEmail(String email);

    List<UserModel> findByPhone(String phone);

    Optional<UserModel> findByEmailAndIsEmailConfirmed(String email, boolean isConfirmed);

    Optional<UserModel> findByPhoneAndIsPhoneConfirmed(String phone, boolean isConfirmed);

    Optional<UserModel> findById(UUID id);

    UserModel getById(UUID id);

    UserModel save(UserModel user);

    void delete(UserModel user);

    List<UserModel> findAll();
}
