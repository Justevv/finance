package com.manager.finance.infrastructure.adapter.in.rest.controller;

import com.manager.finance.application.port.in.PlaceUseCase;
import com.manager.finance.domain.model.PlaceModel;
import com.manager.finance.infrastructure.adapter.in.rest.error.ErrorHelper;
import com.manager.finance.infrastructure.adapter.in.rest.mapper.DtoMapper;
import com.manager.finance.infrastructure.adapter.in.rest.dto.request.PlaceRequestDTO;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.PlaceResponseDTO;
import com.manager.finance.metric.TrackExecutionTime;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/v1/place")
@Slf4j
@RequiredArgsConstructor
public class Place {
    private final PlaceUseCase placeUseCase;
    //    private final CreateReadApiResponse<PlaceRequestDTO, PlaceResponseDTO, PlaceModel> crudApiResponse;
    private final ErrorHelper errorHelper;
    private final DtoMapper<PlaceRequestDTO, PlaceResponseDTO, PlaceModel> dtoMapper;


    @GetMapping
    @TrackExecutionTime
    public ResponseEntity<Object> getPlaces(Principal principal) {
        return ResponseEntity.ok(placeUseCase.getAll(principal));
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getPlace(Principal principal, @PathVariable("id") String id) {
        ResponseEntity<Object> responseEntity;
        try {
            UUID uuid = UUID.fromString(id);
            var model = placeUseCase.get(uuid, principal);
            if (model != null) {
                var response = dtoMapper.toResponseDto(model);
                responseEntity = ResponseEntity.ok(response);
            } else {
                responseEntity = new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            responseEntity = new ResponseEntity<>("Invalid UUID", HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PostMapping
    @TrackExecutionTime
    public ResponseEntity<Object> createPlace(Principal principal, @RequestBody @Valid PlaceRequestDTO placeDTO, BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(placeUseCase.create(principal, dtoMapper.toModel(placeDTO)));
        }
        return responseEntity;
    }

}

