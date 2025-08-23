package com.manager.finance.service;

import com.manager.finance.dto.ExpenseDTO;
import com.manager.finance.dto.response.ExpenseResponseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.helper.UserHelper;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.repository.CategoryRepository;
import com.manager.finance.repository.ExpenseRepository;
import com.manager.finance.repository.PlaceRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ExpenseService implements CrudService<ExpenseEntity, ExpenseDTO, ExpenseResponseDTO> {
    private static final String ENTITY_TYPE_NAME = "expense";
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final PlaceService placeService;
    private final PlaceRepository placeRepository;
    private final CrudLogConstants crudLogConstants;
    @Getter
    private final ModelMapper mapper;
    private final UserHelper userHelper;

    public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository, CategoryService categoryService, PlaceService placeService, PlaceRepository placeRepository,
                          ModelMapper mapper, UserHelper userHelper) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.placeService = placeService;
        this.placeRepository = placeRepository;
        this.mapper = mapper;
        this.userHelper = userHelper;
        crudLogConstants = new CrudLogConstants(ENTITY_TYPE_NAME);
    }

    @TrackExecutionTime
    @Override
    public ExpenseResponseDTO get(ExpenseEntity entity) {
        return convertEntityToResponseDTO(entity);
    }

    @TrackExecutionTime
    @Cacheable(cacheNames = "expense")
    public List<ExpenseResponseDTO> getAll(int page, int countPerPage, Principal principal) {
        var user = userHelper.getUser(principal);
        log.debug("Input start page is {}, count on page is {}", page, countPerPage);
        Pageable pageable = PageRequest.of(page, countPerPage);
        var expenseEntities = expenseRepository.findByUser(user, pageable);
        log.debug(crudLogConstants.getListFiltered(), expenseEntities);
        return expenseEntities.stream().map(this::convertEntityToResponseDTO).toList();
    }


    @TrackExecutionTime
    public List<ExpenseResponseDTO> getAll(Principal principal) {
        var user = userHelper.getUser(principal);
        var expenseEntities = expenseRepository.findByUser(user);
        log.debug(crudLogConstants.getListFiltered(), expenseEntities);
        return expenseEntities.stream().map(this::convertEntityToResponseDTO).toList();
    }

    @Override
    @TrackExecutionTime
    @Transactional
    public ExpenseResponseDTO create(Principal principal, ExpenseDTO expenseDTO) {
        log.debug(crudLogConstants.getInputNewDTO(), expenseDTO);
        var expense = getMapper().map(expenseDTO, ExpenseEntity.class);
        expense.setGuid(UUID.randomUUID());
        expense.setUser(userHelper.getUser(principal));
        expense.setDate(LocalDateTime.now());
        expense.setCategory(categoryService.getOrCreate(principal, expenseDTO.getCategory()));
        expense.setPlace(placeService.getOrCreate(principal, expenseDTO.getPlace()));
        expenseRepository.save(expense);
        log.info(crudLogConstants.getSaveEntityToDatabase(), expense);
        return convertEntityToResponseDTO(expense);
    }


    @Override
    @TrackExecutionTime
    public ExpenseResponseDTO update(ExpenseEntity expense, ExpenseDTO expenseDTO) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), expenseDTO, expense);
        getMapper().map(expenseDTO, expense);
//        setDefaultValue(expense);
        expenseRepository.save(expense);
        log.info(crudLogConstants.getSaveEntityToDatabase(), expense);
        return convertEntityToResponseDTO(expense);
    }

    @Override
    @TrackExecutionTime
    public Void delete(ExpenseEntity expenseEntity) {
        log.debug(crudLogConstants.getDeleteEntityFromDatabase(), expenseEntity);
        expenseRepository.delete(expenseEntity);
        return null;
    }

    @TrackExecutionTime
    public double getSum() {
        return expenseRepository.selectSum();
    }

    @TrackExecutionTime
    public double getSum(CategoryEntity categoryEntity) {
        return expenseRepository.selectSum(categoryEntity);
    }

    @TrackExecutionTime
    private ExpenseResponseDTO convertEntityToResponseDTO(ExpenseEntity expense) {
        var responseDTO = mapper.map(expense, ExpenseResponseDTO.class);
        log.debug(crudLogConstants.getOutputDTOAfterMapping(), responseDTO);
        return responseDTO;
    }
}


