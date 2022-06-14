package com.manager.finance.controller;

import com.manager.finance.config.CrudLogConstants;
import com.manager.finance.dto.CrudDTO;
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
    private final CrudLogConstants crudLogConstants;
    private final T model;

    public CrudApiResponse(T model, String type) {
        this.model = model;
        crudLogConstants = new CrudLogConstants(type);
    }

    public ResponseEntity<Object> create(CrudDTO dto, Principal principal, BindingResult bindingResult) throws UserPrincipalNotFoundException {
        log.debug(crudLogConstants.getInputDataNew(), dto);
        log.debug("Current principal is {}", principal);
        if (principal == null) {
            throw new UsernameNotFoundException("Principal is null");
        }
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            var entity = model.create(dto, principal);
            log.debug(crudLogConstants.getSaveToDatabase(), entity);
            responseEntity = ResponseEntity.ok(entity);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorAdd(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(crudLogConstants.getSavedResponse(), responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Object> update(CrudEntity entity, CrudDTO dto, BindingResult bindingResult) {
        log.debug(crudLogConstants.getInputDataToChange(), entity, dto);
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(model.update(entity, dto));
            log.debug(crudLogConstants.getSaveToDatabase(), dto);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorChange(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(crudLogConstants.getUpdatedResponse(), responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Object> delete(CrudEntity entity) {
        log.debug(crudLogConstants.getInputDataForDelete(), entity);
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(model.delete(entity));
        log.debug(crudLogConstants.getDeletedResponse(), responseEntity);
        return responseEntity;
    }

}
