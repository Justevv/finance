package com.manager.finance.infrastructure.adapter.out.persistence.mapper;

import com.manager.finance.domain.model.CategoryModel;
import com.manager.finance.domain.model.ExpenseModel;
import com.manager.finance.domain.model.PlaceModel;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.ExpenseEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.PlaceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseEntityMapper implements EntityMapper<ExpenseEntity, ExpenseModel> {
    private final EntityMapper<CategoryEntity, CategoryModel> categoryMapper;
    private final EntityMapper<PlaceEntity, PlaceModel> placeMapper;

    @Override
    public ExpenseModel toModel(ExpenseEntity entity) {
        if (entity == null) {
            return null;
        }
        return ExpenseModel.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .date(entity.getDate())
                .category(categoryMapper.toModel(entity.getCategory()))
                .place(placeMapper.toModel(entity.getPlace()))
//                .paymentType(entity.paymentType())
                .amount(entity.getAmount())
//                .account(entity.getAccount())
                .transactionType(entity.getTransactionType())
                .userId(entity.getUserId())
                .build();
    }

    @Override
    public ExpenseEntity toEntity(ExpenseModel model) {
        if (model == null) {
            return null;
        }
        return ExpenseEntity.builder()
                .id(model.id())
                .description(model.description())
                .date(model.date())
                .category(categoryMapper.toEntity(model.category()))
                .place(placeMapper.toEntity(model.place()))
//                .paymentType(model.paymentType())
                .amount(model.amount())
//                .account(model.getAccount())
                .transactionType(model.transactionType())
                .userId(model.userId())
                .build();
    }
}
