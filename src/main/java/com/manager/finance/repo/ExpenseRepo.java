package com.manager.finance.repo;

import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseRepo extends JpaRepository<ExpenseEntity, Long> {

    @Query("SELECT SUM(ee.sum) FROM ExpenseEntity ee")
    double selectSum();

    @Query("SELECT SUM(ee.sum) FROM ExpenseEntity ee where ee.category = ?1")
    double selectSum(CategoryEntity categoryEntity);
}
