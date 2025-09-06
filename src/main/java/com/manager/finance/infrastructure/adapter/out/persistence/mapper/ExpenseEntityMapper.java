package com.manager.finance.infrastructure.adapter.out.persistence.mapper;

import com.manager.finance.domain.model.CategoryModel;
import com.manager.finance.domain.model.ExpenseModel;
import com.manager.finance.domain.model.PlaceModel;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.ExpenseEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.PlaceEntity;
import com.manager.user.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseEntityMapper implements EntityMapper<ExpenseEntity, ExpenseModel> {
    private final EntityMapper<CategoryEntity, CategoryModel> categoryMapper;
    private final EntityMapper<PlaceEntity, PlaceModel> placeMapper;
    private final UserHelper userHelper;

    @Override
    public ExpenseModel toModel(ExpenseEntity dto) {
        if (dto == null) {
            return null;
        }
        return ExpenseModel.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .date(dto.getDate())
                .category(categoryMapper.toModel(dto.getCategory()))
                .place(placeMapper.toModel(dto.getPlace()))
//                .paymentType(dto.paymentType())
                .amount(dto.getAmount())
//                .account(dto.getAccount())
                .transactionType(dto.getTransactionType())
//                .user()
                .build();
    }

    @Override
    public ExpenseEntity toEntity(ExpenseModel dto) {
        if (dto == null) {
            return null;
        }
        return ExpenseEntity.builder()
                .id(dto.id())
                .description(dto.description())
                .date(dto.date())
                .category(categoryMapper.toEntity(dto.category()))
                .place(placeMapper.toEntity(dto.place()))
//                .paymentType(dto.paymentType())
                .amount(dto.amount())
//                .account(dto.getAccount())
                .transactionType(dto.transactionType())
                .user(userHelper.toEntity(dto.user()))
                .build();
    }
}
