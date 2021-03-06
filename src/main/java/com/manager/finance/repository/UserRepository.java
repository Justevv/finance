package com.manager.finance.repository;

import com.manager.finance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findByEmail(String email);

    List<UserEntity> findByPhone(String phone);

    Optional<UserEntity> findByEmailAndIsEmailConfirmed(String email, boolean isConfirmed);

    Optional<UserEntity> findByPhoneAndIsPhoneConfirmed(String phone, boolean isConfirmed);


}
