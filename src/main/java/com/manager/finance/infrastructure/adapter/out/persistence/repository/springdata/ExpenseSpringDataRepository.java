package com.manager.finance.infrastructure.adapter.out.persistence.repository.springdata;

import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.ExpenseEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.finance.metric.TrackExecutionTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseSpringDataRepository extends JpaRepository<ExpenseEntity, UUID> {

    @TrackExecutionTime
    List<ExpenseEntity> findByUserId(UUID userId);

    @TrackExecutionTime
    Optional<ExpenseEntity> findByIdAndUserId(UUID id, UUID userId);

    @TrackExecutionTime
    boolean existsByIdAndUserId(UUID id, UUID userId);

    @TrackExecutionTime
    List<ExpenseEntity> findByUserId(UUID userId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM ExpenseEntity ee WHERE ee.id = ?1 AND ee.userId = ?2")
    void deleteById(UUID id, UserEntity user);

    @TrackExecutionTime
    @Query("SELECT SUM(ee.amount) FROM ExpenseEntity ee where ee.userId = ?1")
    double selectAmount(Principal principal);

    @TrackExecutionTime
    @Query("SELECT SUM(ee.amount) FROM ExpenseEntity ee where ee.category = ?1 and ee.userId = ?2")
    double selectAmount(Principal principal, CategoryEntity categoryEntity);

}
