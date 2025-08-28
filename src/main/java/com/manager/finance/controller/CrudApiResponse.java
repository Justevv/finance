package com.manager.finance.controller;

import com.manager.finance.dto.BaseCrudDTO;
import com.manager.finance.dto.response.BaseCrudResponseDTO;
import com.manager.finance.entity.CrudEntity;
import com.manager.finance.exception.EntityNotFoundException;
import com.manager.finance.helper.ErrorHelper;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.UUID;

public class CrudApiResponse<D extends BaseCrudDTO, R extends BaseCrudResponseDTO> {
    private final CrudService<D, R> model;
    @Autowired
    private ErrorHelper errorHelper;

    public CrudApiResponse(CrudService<D, R> model) {
        this.model = model;
    }

    @TrackExecutionTime
    public ResponseEntity<Object> get(String id, Principal principal) {
        ResponseEntity<Object> responseEntity;
        try {
            UUID uuid = UUID.fromString(id);
            R body = model.get(uuid, principal);
            if (body != null) {
                responseEntity = ResponseEntity.ok(body);
            } else {
                responseEntity = new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            responseEntity = new ResponseEntity<>("Invalid UUID", HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @TrackExecutionTime
    public ResponseEntity<Object> getAll(Principal principal) {
        return ResponseEntity.ok(model.getAll(principal));
    }

    @TrackExecutionTime
    public ResponseEntity<Object> create(Principal principal, D dto, BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(model.create(principal, dto));
        }
        return responseEntity;
    }

    @TrackExecutionTime
    public ResponseEntity<Object> update(String id, Principal principal, D dto, BindingResult bindingResult) {
        ResponseEntity<Object> responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            try {
                UUID uuid = UUID.fromString(id);
                responseEntity = ResponseEntity.ok(model.update(uuid, principal, dto));
            } catch (IllegalArgumentException e) {
                responseEntity = new ResponseEntity<>("Invalid UUID", HttpStatus.BAD_REQUEST);
            } catch (EntityNotFoundException e) {
                responseEntity = new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
            } catch (UsernameNotFoundException e) {
                responseEntity = new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        }
        return responseEntity;
    }

    @TrackExecutionTime
    public ResponseEntity<Object> delete(String id, Principal principal) {
        ResponseEntity<Object> responseEntity;
        try {
            UUID uuid = UUID.fromString(id);
            model.delete(uuid, principal);
            responseEntity = ResponseEntity.ok(null);
        } catch (IllegalArgumentException e) {
            responseEntity = new ResponseEntity<>("Invalid UUID", HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            responseEntity = new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
        } catch (UsernameNotFoundException e) {
            responseEntity = new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

}