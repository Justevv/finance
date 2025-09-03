package com.manager.finance.controller;

import com.manager.finance.infrastructure.controller.dto.request.BaseCrudDTO;
import com.manager.finance.infrastructure.controller.dto.response.BaseCrudResponseDTO;
import com.manager.finance.infrastructure.controller.error.ErrorHelper;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.application.serivice.CreateReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.UUID;

public class CreateReadApiResponse<D extends BaseCrudDTO, R extends BaseCrudResponseDTO> {
    private final CreateReadService<D, R> model;
    @Autowired
    private ErrorHelper errorHelper;

    public CreateReadApiResponse(CreateReadService<D, R> model) {
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

}