package com.manager.finance.repository;

import com.manager.finance.entity.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<PlaceEntity, Long> {
//    PlaceEntity findById(long id);

    PlaceEntity findByName(String name);
}
