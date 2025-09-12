package com.manager.finance.application.port.in;

import com.manager.finance.domain.model.CategoryModel;

import java.util.List;
import java.util.UUID;

public interface CategoryUseCase {

    List<CategoryModel> getPage(int page, int countPerPage);

    List<CategoryModel> getAll(UUID userId);

    CategoryModel get(UUID uuid, UUID userId);

    CategoryModel create(UUID userId, CategoryModel model);
}
