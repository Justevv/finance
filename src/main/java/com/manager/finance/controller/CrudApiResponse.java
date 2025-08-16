package com.manager.finance.controller;

import com.manager.finance.dto.BaseCrudDTO;
import com.manager.finance.dto.response.BaseCrudResponseDTO;
import com.manager.finance.entity.CrudEntity;
import com.manager.finance.helper.ErrorHelper;
import com.manager.finance.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.security.Principal;

public class CrudApiResponse<E extends CrudEntity, D extends BaseCrudDTO, R extends BaseCrudResponseDTO> {
    private final CrudService<E, D, R> model;
    @Autowired
    private ErrorHelper errorHelper;

    public CrudApiResponse(CrudService<E, D, R> model) {
        this.model = model;
    }

    public ResponseEntity<Object> get(Principal principal, E entity) {
        var responseEntity = errorHelper.checkEntityNotBelongPrincipal(principal, entity);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(model.get(entity));
        }
        return responseEntity;
    }

    public ResponseEntity<Object> getAll(Principal principal) {
        return ResponseEntity.ok(model.getAll(principal));
    }

    public ResponseEntity<Object> create(Principal principal, D dto, BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(model.create(principal, dto));
        }
        return responseEntity;
    }

    public ResponseEntity<Object> update(Principal principal, E entity, D dto, BindingResult bindingResult) {
        var responseEntity = errorHelper.checkEntityNotBelongPrincipal(principal, entity);
        if (responseEntity == null) {
            responseEntity = errorHelper.checkErrors(bindingResult);
        }
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(model.update(entity, dto));
        }
        return responseEntity;
    }

    public ResponseEntity<Object> delete(Principal principal, E entity) {
        var responseEntity = errorHelper.checkEntityNotBelongPrincipal(principal, entity);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(model.delete(entity));
        }
        return responseEntity;
    }

}