package com.manager.repo;

import com.manager.entity.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepo extends JpaRepository<PlaceEntity, Long> {
//    PlaceEntity findById(long id);

    PlaceEntity findByName(String name);
}
