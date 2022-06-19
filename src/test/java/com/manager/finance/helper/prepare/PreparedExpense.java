package com.manager.finance.helper.prepare;

import com.manager.finance.entity.ExpenseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

@TestConfiguration
@Import({PreparedUser.class, PreparedCategory.class, PreparedPlace.class})
public class PreparedExpense {
    @Autowired
    private PreparedUser preparedUser;
    @Autowired
    private PreparedCategory preparedCategory;
    @Autowired
    private PreparedPlace preparedPlace;

    public ExpenseEntity createExpense() {
        var expenseEntity = new ExpenseEntity();
        expenseEntity.setId(1);
        expenseEntity.setCategory(preparedCategory.createCategory());
        expenseEntity.setPlace(preparedPlace.createPlace());
        expenseEntity.setDate(LocalDateTime.now());
        expenseEntity.setDescription("expenseDescription");
        expenseEntity.setSum(700);
        expenseEntity.setUser(preparedUser.createUser());
        return expenseEntity;
    }

}

