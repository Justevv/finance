package com.manager.finance.helper.prepare;

import com.manager.finance.entity.ExpenseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

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
        expenseEntity.setId(1);
        expenseEntity.setCategory(categoryPrepareHelper.createCategory());
        expenseEntity.setPlace(placePrepareHelper.createPlace());
        expenseEntity.setDate(LocalDateTime.now());
        expenseEntity.setDescription("expenseDescription");
        expenseEntity.setSum(700);
        expenseEntity.setUser(userPrepareHelper.createUser());
        return expenseEntity;
    }

}

