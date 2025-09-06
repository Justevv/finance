package com.manager.finance.infrastructure.adapter.in.rest.mapper;

import com.manager.finance.domain.model.CategoryModel;
import com.manager.finance.domain.model.ExpenseModel;
import com.manager.finance.domain.model.PlaceModel;
import com.manager.finance.infrastructure.adapter.in.rest.dto.request.CategoryRequestDTO;
import com.manager.finance.infrastructure.adapter.in.rest.dto.request.ExpenseRequestDTO;
import com.manager.finance.infrastructure.adapter.in.rest.dto.request.PlaceRequestDTO;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.CategoryResponseDTO;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.ExpenseResponseDTO;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.PlaceResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseDtoMapper implements DtoMapper<ExpenseRequestDTO, ExpenseResponseDTO, ExpenseModel> {
    private final DtoMapper<CategoryRequestDTO, CategoryResponseDTO, CategoryModel> categoryMapper;
    private final DtoMapper<PlaceRequestDTO, PlaceResponseDTO, PlaceModel> placeMapper;

    @Override
    public ExpenseModel toModel(ExpenseRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return ExpenseModel.builder()
                .id(dto.id())
                .description(dto.description())
                .date(dto.date())
                .category(categoryMapper.toModel(dto.category()))
                .place(placeMapper.toModel(dto.place()))
//                .paymentType(dto.paymentType())
                .amount(dto.amount())
//                .account(dto.getAccount())
                .transactionType(dto.transactionType())
//                .user()
                .build();
    }

    @Override
    public ExpenseResponseDTO toResponseDto(ExpenseModel dto) {
        if (dto == null) {
            return null;
        }
        return ExpenseResponseDTO.builder()
                .id(dto.id())
                .description(dto.description())
                .date(dto.date())
                .category(categoryMapper.toResponseDto(dto.category()))
                .place(placeMapper.toResponseDto(dto.place()))
//                .paymentType(dto.getPaymentType())
                .amount(dto.amount())
//                .account(dto.getAccount())
                .transactionType(dto.transactionType())
//                .user()
                .build();
    }
}
