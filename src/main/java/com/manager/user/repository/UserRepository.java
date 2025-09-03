package com.manager.user.repository;

import com.manager.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findByEmail(String email);

    List<UserEntity> findByPhone(String phone);

    Optional<UserEntity> findByEmailAndIsEmailConfirmed(String email, boolean isConfirmed);

    Optional<UserEntity> findByPhoneAndIsPhoneConfirmed(String phone, boolean isConfirmed);


}
