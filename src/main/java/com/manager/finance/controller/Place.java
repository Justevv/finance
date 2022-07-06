package com.manager.finance.controller;

import com.manager.finance.dto.PlaceDTO;
import com.manager.finance.dto.response.PlaceResponseDTO;
import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.model.PlaceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/v1/place")
@Slf4j
public class Place extends CrudApiResponse<PlaceEntity, PlaceDTO, PlaceResponseDTO> {

    public Place(PlaceModel placeModel) {
        super(placeModel);
    }

    @GetMapping
    public ResponseEntity<Object> getPlace(Principal principal) {
        return getAll(principal);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getPlaces(Principal principal, @PathVariable("id") PlaceEntity place) {
        return get(principal, place);
    }

    @PostMapping
    public ResponseEntity<Object> createPlace(Principal principal, @Valid PlaceDTO placeDTO, BindingResult bindingResult) {
        return create(principal, placeDTO, bindingResult);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updatePlace(Principal principal, @PathVariable("id") PlaceEntity placeEntity, @Valid PlaceDTO placeDTO,
                                              BindingResult bindingResult) {
        return update(principal, placeEntity, placeDTO, bindingResult);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletePlace(Principal principal, @PathVariable("id") PlaceEntity placeEntity) {
        return delete(principal, placeEntity);
    }

}

