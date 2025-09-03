package com.manager.finance.infrastructure.controller.rest;

import com.manager.finance.controller.CreateReadApiResponse;
import com.manager.finance.infrastructure.controller.dto.request.PlaceDTO;
import com.manager.finance.infrastructure.controller.dto.response.PlaceResponseDTO;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.application.serivice.PlaceService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/v1/place")
@Slf4j
public class Place extends CreateReadApiResponse<PlaceDTO, PlaceResponseDTO> {

    public Place(PlaceService placeService) {
        super(placeService);
    }

    @GetMapping
    @TrackExecutionTime
    public ResponseEntity<Object> getPlace(Principal principal) {
        return getAll(principal);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getPlaces(Principal principal, @PathVariable("id") String id) {
        return get(id, principal);
    }

    @PostMapping
    @TrackExecutionTime
    public ResponseEntity<Object> createPlace(Principal principal, @RequestBody @Valid PlaceDTO placeDTO, BindingResult bindingResult) {
        return create(principal, placeDTO, bindingResult);
    }

}

