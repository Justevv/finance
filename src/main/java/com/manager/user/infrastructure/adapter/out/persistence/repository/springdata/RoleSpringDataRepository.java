package com.manager.user.infrastructure.adapter.out.persistence.repository.springdata;


import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleSpringDataRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByName(String roleName);

}
