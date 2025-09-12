package com.manager.finance.application.port.in;

import com.manager.finance.domain.model.ExpenseModel;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.manager.user.domain.model.UserModel;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface ExpenseUseCase {
    ExpenseModel get(UUID id, UUID userId);

    ExpenseModel create(UUID userId, ExpenseModel expenseModel);

    ExpenseModel update(UUID id, UUID userId, ExpenseModel expenseRequestDTO);

    ExpenseModel patch(UUID id, UUID userId, ExpenseModel input);

    void delete(UUID id, Principal principal);

    List<ExpenseModel> getAll(int page, int countPerPage, UUID userId);

    double getSum(Principal principal);

    double getSum(Principal principal, CategoryEntity categoryEntity);

    void checkExpense(UUID id, Principal principal);

    List<ExpenseModel> getAll(Principal principal);
}
