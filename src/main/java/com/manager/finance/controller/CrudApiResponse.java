package com.manager.finance.controller;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.DTO;
import com.manager.finance.entity.CrudEntity;
import com.manager.finance.model.CrudModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.util.Map;

@Slf4j
public class CrudApiResponse<T extends CrudModel> {
    private final LogConstants logConstants;
    private final T model;

    public CrudApiResponse(T model, String type) {
        this.model = model;
        logConstants = new LogConstants(type);
    }

    public ResponseEntity<Object> create(DTO dto, Principal principal, BindingResult bindingResult) throws UserPrincipalNotFoundException {
        log.debug(logConstants.getInputDataNew(), dto);
        log.debug("Current principal is {}", principal);
        if (principal == null) {
            throw new UsernameNotFoundException("Principal is null");
        }
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            var entity = model.create(dto, principal);
            log.debug(logConstants.getSaveToDatabase(), entity);
            responseEntity = ResponseEntity.ok(entity);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(logConstants.getErrorAdd(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(logConstants.getSavedResponse(), responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Object> update(CrudEntity entity, DTO dto, BindingResult bindingResult) {
        log.debug(logConstants.getInputDataToChange(), entity, dto);
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(model.update(entity, dto));
            log.debug(logConstants.getSaveToDatabase(), dto);
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
