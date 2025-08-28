package com.manager.finance.repository;

import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.metric.TrackExecutionTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, UUID> {

    @TrackExecutionTime
    List<ExpenseEntity> findByUser(UserEntity userEntity);

    @TrackExecutionTime
    Optional<ExpenseEntity> findByIdAndUser(UUID id, UserEntity userEntity);

    @TrackExecutionTime
    boolean existsByIdAndUser(UUID id, UserEntity userEntity);

    @TrackExecutionTime
    List<ExpenseEntity> findByUser(UserEntity userEntity, Pageable pageable);

    @Modifying
    @Query("DELETE FROM ExpenseEntity ee WHERE ee.id = ?1 AND ee.user = ?2")
    void deleteById(UUID id, UserEntity user);

    @TrackExecutionTime
    @Query("SELECT SUM(ee.sum) FROM ExpenseEntity ee where ee.user = ?1")
    double selectSum(Principal principal);

    @TrackExecutionTime
    @Query("SELECT SUM(ee.sum) FROM ExpenseEntity ee where ee.category = ?1 and ee.user = ?2")
    double selectSum(Principal principal, CategoryEntity categoryEntity);

}
