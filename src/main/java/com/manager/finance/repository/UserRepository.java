package com.manager.finance.repository;

import com.manager.finance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByPhone(String phone);

    List<UserEntity> findByEmailAndIsEmailConfirmed(String email, boolean isConfirmed);

    List<UserEntity> findByPhoneAndIsPhoneConfirmed(String phone, boolean isConfirmed);


}
