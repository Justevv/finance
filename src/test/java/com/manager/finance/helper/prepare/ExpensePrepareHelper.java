package com.manager.finance.helper.prepare;

import com.manager.finance.infrastructure.adapter.out.persistence.entity.ExpenseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@TestConfiguration
@Import({UserPrepareHelper.class, CategoryPrepareHelper.class, PlacePrepareHelper.class})
public class ExpensePrepareHelper {
    @Autowired
    private UserPrepareHelper userPrepareHelper;
    @Autowired
    private CategoryPrepareHelper categoryPrepareHelper;
    @Autowired
    private PlacePrepareHelper placePrepareHelper;

    public ExpenseEntity createExpense() {
        var expenseEntity = new ExpenseEntity();
        expenseEntity.setId(UUID.randomUUID());
        expenseEntity.setCategory(categoryPrepareHelper.createCategory());
        expenseEntity.setPlace(placePrepareHelper.createPlace());
        expenseEntity.setDate(LocalDateTime.now());
        expenseEntity.setDescription("expenseDescription");
        expenseEntity.setAmount(BigDecimal.valueOf(700));
        expenseEntity.setUser(userPrepareHelper.createUser());
        return expenseEntity;
    }

}

