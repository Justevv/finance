package com.manager.finance.repo;

import com.manager.finance.entity.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepo extends JpaRepository<PlaceEntity, Long> {
//    PlaceEntity findById(long id);

    PlaceEntity findByName(String name);
}
