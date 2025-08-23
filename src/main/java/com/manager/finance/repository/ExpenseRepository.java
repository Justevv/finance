package com.manager.finance.repository;

import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.metric.TrackExecutionTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, UUID> {

    @TrackExecutionTime
    List<ExpenseEntity> findByUser(UserEntity userEntity);

    @TrackExecutionTime
    List<ExpenseEntity> findByUser(UserEntity userEntity, Pageable topTen);

    @TrackExecutionTime
    @Query("SELECT SUM(ee.sum) FROM ExpenseEntity ee")
    double selectSum();

    @TrackExecutionTime
    @Query("SELECT SUM(ee.sum) FROM ExpenseEntity ee where ee.category = ?1")
    double selectSum(CategoryEntity categoryEntity);

}
