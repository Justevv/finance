package com.manager.finance.controller;

import com.manager.finance.dto.BaseCrudDTO;
import com.manager.finance.dto.response.BaseCrudResponseDTO;
import com.manager.finance.entity.CrudEntity;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.model.CrudModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.security.Principal;

@Slf4j
public class CrudApiResponse<E extends CrudEntity, D extends BaseCrudDTO, R extends BaseCrudResponseDTO> {
    private final CrudLogConstants crudLogConstants;
    private final CrudModel<E, D, R> model;

    public CrudApiResponse(CrudModel<E, D, R> model) {
        this.model = model;
        crudLogConstants = new CrudLogConstants(model.getEntityTypeName());
    }

    public ResponseEntity<Object> get(Principal principal, E entity) {
        var responseEntity = checkEntityBelongPrincipal(principal, entity);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(model.get(entity));
        }
        return responseEntity;
    }

    public ResponseEntity<Object> getAll(Principal principal) {
        return ResponseEntity.ok(model.getAll(principal));
    }

    public ResponseEntity<Object> create(Principal principal, D dto, BindingResult bindingResult) {
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            var entity = model.create(principal, dto);
            responseEntity = ResponseEntity.ok(entity);
        } else {
            var errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorsAdded(), errors);
            responseEntity = ResponseEntity.badRequest().body(errors);
        }
        return responseEntity;
    }


    public ResponseEntity<Object> update(Principal principal, E entity, D dto, BindingResult bindingResult) {
        var responseEntity = checkEntityBelongPrincipal(principal, entity);
        if (responseEntity == null) {
            if (!bindingResult.hasErrors()) {
                responseEntity = ResponseEntity.ok(model.update(entity, dto));
            } else {
                var errors = Utils.getErrors(bindingResult);
                log.debug(crudLogConstants.getErrorsAdded(), errors);
                responseEntity = ResponseEntity.badRequest().body(errors);
            }
        }
        return responseEntity;
    }

    public ResponseEntity<Object> delete(Principal principal, E entity) {
        var responseEntity = checkEntityBelongPrincipal(principal, entity);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(model.delete(entity));
        }
        return responseEntity;
    }

    public ResponseEntity<Object> checkEntityBelongPrincipal(Principal principal, CrudEntity entity) {
        ResponseEntity<Object> responseEntity = null;
        if (!isEntityBelongPrincipal(principal, entity)) {
            responseEntity = new ResponseEntity<>(crudLogConstants.getEntityTypeNotFound(), HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    private boolean isEntityBelongPrincipal(Principal principal, CrudEntity entity) {
        var username = principal.getName();
        var entityOwner = entity.getUser().getUsername();
        log.trace("Current username is {}, entity {} belongs {}", username, entity, entityOwner);
        return username.equals(entityOwner);
    }

}