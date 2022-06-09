package com.manager.finance.controller;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.DTO;
import com.manager.finance.entity.CrudEntity;
import com.manager.finance.model.CrudModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Map;

@Slf4j
public class CrudResponseApi<T extends CrudModel> {
    private final LogConstants logConstants;
    private final T model;

    public CrudResponseApi(T model, String type) {
        this.model = model;
        logConstants = new LogConstants(type);
    }

    public ResponseEntity<Object> create(DTO categoryDTO, BindingResult bindingResult) {
        log.debug(logConstants.getInputDataNew(), categoryDTO);
        ResponseEntity<Object> responseEntity;

        if (!bindingResult.hasErrors()) {
            CrudEntity category = model.create(categoryDTO);
            log.debug(logConstants.getSaveToDatabase(), category);
            responseEntity = ResponseEntity.ok(category);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(logConstants.getErrorAdd(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(logConstants.getSavedResponse(), responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Object> update(CrudEntity entity, DTO expenseDTO, BindingResult bindingResult) {
        log.debug(logConstants.getInputDataToChange(), entity, expenseDTO);
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(model.update(entity, expenseDTO));
            log.debug(logConstants.getSaveToDatabase(), expenseDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(logConstants.getErrorChange(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(logConstants.getUpdatedResponse(), responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Object> delete(CrudEntity entity) {
        log.debug(logConstants.getInputDataForDelete(), entity);
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(model.delete(entity));
        log.debug(logConstants.getDeletedResponse(), responseEntity);
        return responseEntity;
    }

}
