package com.manager.finance.service;

import com.manager.finance.dto.ExpenseDTO;
import com.manager.finance.dto.response.ExpenseResponseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.helper.UserHelper;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.redis.ExpenseRedisRepository;
import com.manager.finance.repository.ExpenseRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
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
public class ExpenseService implements CrudService<ExpenseEntity, ExpenseDTO, ExpenseResponseDTO> {
    private static final String ENTITY_TYPE_NAME = "expense";
    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;
    private final PlaceService placeService;
    @Getter
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
    public ExpenseResponseDTO get(ExpenseEntity entity) {
        return convertEntityToResponseDTO(entity);
    }

    @TrackExecutionTime
    public ExpenseResponseDTO get(UUID entity, Principal principal) {
        var user = userHelper.getUser(principal);
        var expense = expenseRedisRepository.findByGuidAndUserGuid(entity, user.getGuid());
        ExpenseResponseDTO expenseResponseDTO = null;
        if (expense.isPresent()) {
            expenseResponseDTO = expense.get();
            log.debug(crudLogConstants.getGetDTOFromCache(), expenseResponseDTO);
        } else {
            var expenseEntity = expenseRepository.findByGuidAndUser(entity, user);
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
        ExpenseResponseDTO expenseResponseDTO = convertEntityToResponseDTO(expense);
        expenseRedisRepository.save(expenseResponseDTO);
        return expenseResponseDTO;
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


