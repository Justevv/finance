package com.manager.finance.repository;

import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<PlaceEntity, Long> {
//    PlaceEntity findById(long id);

    PlaceEntity findByName(String name);

    List<PlaceEntity> findByUser(UserEntity user);
}
