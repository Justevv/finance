package com.manager.finance.model;

import com.manager.finance.dto.ExpenseDTO;
import com.manager.finance.dto.response.ExpenseResponseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.helper.UserHelper;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.repository.CategoryRepository;
import com.manager.finance.repository.ExpenseRepository;
import com.manager.finance.repository.PlaceRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ExpenseModel implements CrudModel<ExpenseEntity, ExpenseDTO, ExpenseResponseDTO> {
    private static final String EXPENSE_LOG_NAME = "expense";
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final PlaceRepository placeRepository;
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(EXPENSE_LOG_NAME);
    @Getter
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private UserHelper userHelper;

    public ExpenseModel(ExpenseRepository expenseRepository, CategoryRepository categoryRepository, PlaceRepository placeRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public ExpenseResponseDTO get(ExpenseEntity entity) {
        return convertEntityToResponseDTO(entity);
    }

    @Cacheable(cacheNames = "expense")
    public List<ExpenseResponseDTO> getAll(int page, int countPerPage, Principal principal) {
        var user = userHelper.getUser(principal);
        log.debug("Input start page is {}, count on page is {}", page, countPerPage);
        Pageable pageable = PageRequest.of(page, countPerPage);
        var expenseEntities = expenseRepository.findByUser(user, pageable);
        log.debug(crudLogConstants.getListFiltered(), expenseEntities);
        return expenseEntities.stream().map(this::convertEntityToResponseDTO).toList();
    }


    public List<ExpenseResponseDTO> getAll(Principal principal) {
        var user = userHelper.getUser(principal);
        var expenseEntities = expenseRepository.findByUser(user);
        log.debug(crudLogConstants.getListFiltered(), expenseEntities);
        return expenseEntities.stream().map(this::convertEntityToResponseDTO).toList();
    }

    @Override
    public ExpenseResponseDTO create(Principal principal, ExpenseDTO expenseDTO) {
        log.debug(crudLogConstants.getInputNewDTO(), expenseDTO);
        var expense = getMapper().map(expenseDTO, ExpenseEntity.class);
        expense.setUser(userHelper.getUser(principal));
        expense.setDate(LocalDateTime.now());
        setDefaultValue(expense);
        expenseRepository.save(expense);
        log.info(crudLogConstants.getSaveEntityToDatabase(), expense);
        return convertEntityToResponseDTO(expense);
    }

    private void setDefaultValue(ExpenseEntity expense) {
        if (expense.getCategory() == null) {
            expense.setCategory(categoryRepository.findByName("Default"));
        }
        if (expense.getPlace() == null) {
            expense.setPlace(placeRepository.findByName("Undefined"));
        }
    }

    @Override
    public ExpenseResponseDTO update(ExpenseEntity expense, ExpenseDTO expenseDTO) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), expenseDTO, expense);
        getMapper().map(expenseDTO, expense);
        setDefaultValue(expense);
        expenseRepository.save(expense);
        log.info(crudLogConstants.getSaveEntityToDatabase(), expense);
        return convertEntityToResponseDTO(expense);
    }

    @Override
    public Void delete(ExpenseEntity expenseEntity) {
        log.debug(crudLogConstants.getDeleteEntityFromDatabase(), expenseEntity);
        expenseRepository.delete(expenseEntity);
        return null;
    }

    public double getSum() {
        return expenseRepository.selectSum();
    }

    public double getSum(CategoryEntity categoryEntity) {
        return expenseRepository.selectSum(categoryEntity);
    }

    private ExpenseResponseDTO convertEntityToResponseDTO(ExpenseEntity expense) {
        var responseDTO = mapper.map(expense, ExpenseResponseDTO.class);
        log.debug(crudLogConstants.getOutputDTOAfterMapping(), responseDTO);
        return responseDTO;
    }
}


