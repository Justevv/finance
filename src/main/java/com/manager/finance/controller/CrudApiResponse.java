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
import java.util.List;
import java.util.Map;

@Slf4j
public class CrudApiResponse<T extends CrudModel, V extends CrudEntity> {
    private final CrudLogConstants crudLogConstants;
    private final T model;

    public CrudApiResponse(T model, String type) {
        this.model = model;
        crudLogConstants = new CrudLogConstants(type);
    }

    public List<V> getAll(Principal principal) {
        return model.getAll(principal);
    }

    public ResponseEntity<Object> create(CrudDTO dto, Principal principal, BindingResult bindingResult) throws UserPrincipalNotFoundException {
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
        return responseEntity;
    }

    public ResponseEntity<Object> update(CrudEntity entity, CrudDTO dto, BindingResult bindingResult) {
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(model.update(entity, dto));
            log.debug(crudLogConstants.getSaveToDatabase(), dto);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorChange(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    public ResponseEntity<Void> delete(CrudEntity entity) {
        return ResponseEntity.ok(model.delete(entity));
    }

}
