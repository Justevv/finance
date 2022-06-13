package com.manager.finance.repository;

import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    @Query("SELECT ee FROM ExpenseEntity ee where ee.category = ?1")
    List<ExpenseEntity> findByUser(long skip, long limit, UserEntity userEntity);


    @Query("SELECT SUM(ee.sum) FROM ExpenseEntity ee")
    double selectSum();

    @Query("SELECT SUM(ee.sum) FROM ExpenseEntity ee where ee.category = ?1")
    double selectSum(CategoryEntity categoryEntity);

}
