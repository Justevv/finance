package com.manager.finance.controller;

import com.manager.finance.config.CrudLogConstants;
import com.manager.finance.dto.PlaceDTO;
import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.model.PlaceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/place")
@Slf4j
public class Place extends CrudApiResponse<PlaceModel, PlaceEntity> {
    private static final String PLACE = "place";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(PLACE);

    public Place(PlaceModel placeModel) {
        super(placeModel, PLACE);
    }

    @GetMapping
    public List<PlaceEntity> get(Principal principal) {
        return getAll(principal);
    }

    @GetMapping("{id}")
    public PlaceEntity get(@PathVariable("id") PlaceEntity place) {
        log.debug(crudLogConstants.getInput(), place);
        return place;
    }

    @PostMapping
    public ResponseEntity<Object> createPlace(PlaceDTO placeDTO, Principal principal, BindingResult bindingResult) throws UserPrincipalNotFoundException {
        return create(placeDTO, principal, bindingResult);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updatePlace(@PathVariable("id") PlaceEntity placeEntity, @Valid PlaceDTO placeDTO,
                                              BindingResult bindingResult) {
        return update(placeEntity, placeDTO, bindingResult);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable("id") PlaceEntity placeEntity) {
        return delete(placeEntity);
    }

}

