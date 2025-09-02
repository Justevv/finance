package com.manager.finance.service;

import com.manager.finance.dto.ExpenseDTO;
import com.manager.finance.dto.response.ExpenseResponseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.exception.EntityNotFoundException;
import com.manager.finance.helper.UserHelper;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.log.LogConstants;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.redis.ExpenseRedisRepository;
import com.manager.finance.repository.ExpenseRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
@RequiredArgsConstructor
public class ExpenseService implements CrudService<ExpenseDTO, ExpenseResponseDTO> {
    private static final String ENTITY_TYPE_NAME = "expense";
    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;
    private final PlaceService placeService;
    private final ModelMapper mapper;
    private final UserHelper userHelper;
    private final ExpenseRedisRepository expenseRedisRepository;
    private CrudLogConstants crudLogConstants;

    @PostConstruct
    private void init() {
        crudLogConstants = new CrudLogConstants(ENTITY_TYPE_NAME);
    }

    @TrackExecutionTime
    @Override
    public ExpenseResponseDTO get(UUID id, Principal principal) {
        var user = userHelper.getUser(principal);
        var expense = expenseRedisRepository.findByIdAndUserId(id, user.getId());
        ExpenseResponseDTO expenseResponseDTO = null;
        if (expense.isPresent()) {
            expenseResponseDTO = expense.get();
            log.debug(crudLogConstants.getGetDTOFromCache(), expenseResponseDTO);
        } else {
            var expenseEntity = expenseRepository.findByIdAndUser(id, user);
            if (expenseEntity.isPresent()) {
                expenseResponseDTO = convertEntityToResponseDTO(expenseEntity.get());
                expenseRedisRepository.save(expenseResponseDTO);
                log.debug(crudLogConstants.getLoadEntityFromDatabase(), expenseResponseDTO);
                log.debug(crudLogConstants.getSaveDTOToCache(), expenseResponseDTO);
            }
        }
        return expenseResponseDTO;
    }

    @TrackExecutionTime
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

    @TrackExecutionTime
    @Transactional
    @Override
    public ExpenseResponseDTO create(Principal principal, ExpenseDTO expenseDTO) {
        log.debug(crudLogConstants.getInputNewDTO(), expenseDTO);
        var expense = mapper.map(expenseDTO, ExpenseEntity.class);
        expense.setUser(userHelper.getUser(principal));
        expense.setDate(LocalDateTime.now());
        expense.setCategory(categoryService.getOrCreate(principal, expenseDTO.getCategory()));
        expense.setPlace(placeService.getOrCreate(expenseDTO.getPlace()));
        expenseRepository.save(expense);
        log.info(crudLogConstants.getSaveEntityToDatabase(), expense);
        ExpenseResponseDTO expenseResponseDTO = convertEntityToResponseDTO(expense);
        expenseRedisRepository.save(expenseResponseDTO);
        return expenseResponseDTO;
    }

    @TrackExecutionTime
    @Transactional
    @Override
    public ExpenseResponseDTO update(UUID id, Principal principal, ExpenseDTO expenseDTO) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), expenseDTO, null);
        var expense = expenseRepository.findByIdAndUser(id, userHelper.getUser(principal));
        if (expense.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_TYPE_NAME, id);
        }
        var updatedExpense = expense.get();
        mapper.map(expenseDTO, updatedExpense);
        expenseRepository.save(updatedExpense);
        log.info(crudLogConstants.getSaveEntityToDatabase(), updatedExpense);
        return convertEntityToResponseDTO(updatedExpense);
    }

    @TrackExecutionTime
    @Transactional
    @Override
    public void delete(UUID id, Principal principal) {
        checkExpenseExists(id, principal);
        log.debug(LogConstants.DELETE_ENTITY_FROM_DATABASE_BY_ID, ENTITY_TYPE_NAME, id);
        expenseRepository.deleteById(id, userHelper.getUser(principal));
    }

    @TrackExecutionTime
    public double getSum(Principal principal) {
        return expenseRepository.selectSum(principal);
    }

    @TrackExecutionTime
    public double getSum(Principal principal, CategoryEntity categoryEntity) {
        return expenseRepository.selectSum(principal, categoryEntity);
    }

    private void checkExpenseExists(UUID id, Principal principal) {
        var exist = expenseRepository.existsByIdAndUser(id, userHelper.getUser(principal));
        if (!exist) {
            throw new EntityNotFoundException(ENTITY_TYPE_NAME, id);
        }
    }

    private ExpenseResponseDTO convertEntityToResponseDTO(ExpenseEntity expense) {
        var responseDTO = mapper.map(expense, ExpenseResponseDTO.class);
        log.debug(crudLogConstants.getOutputDTOAfterMapping(), responseDTO);
        return responseDTO;
    }
}


