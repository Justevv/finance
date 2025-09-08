package com.manager.finance.application.port.in;

import com.manager.finance.domain.model.ExpenseModel;
import com.manager.finance.domain.model.UserModel;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface ExpenseUseCase {
    ExpenseModel get(UUID id, Principal principal);

    ExpenseModel create(UserModel principal, ExpenseModel expenseModel);

    ExpenseModel update(UUID id, UserModel principal, ExpenseModel expenseRequestDTO);

    ExpenseModel patch(UUID id, UserModel userModel, ExpenseModel input);

    void delete(UUID id, Principal principal);

    List<ExpenseModel> getAll(int page, int countPerPage, Principal principal);

    double getSum(Principal principal);

    double getSum(Principal principal, CategoryEntity categoryEntity);

    void checkExpense(UUID id, Principal principal);

    List<ExpenseModel> getAll(Principal principal);
}
