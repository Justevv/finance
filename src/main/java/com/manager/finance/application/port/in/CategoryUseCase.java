package com.manager.finance.application.port.in;

import com.manager.finance.domain.model.CategoryModel;
import com.manager.user.domain.model.UserModel;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface CategoryUseCase {

    List<CategoryModel> getPage(int page, int countPerPage);

    List<CategoryModel> getAll(Principal principal);

    CategoryModel get(UUID uuid, Principal principal);

    CategoryModel create(UserModel principal, CategoryModel model);
}
