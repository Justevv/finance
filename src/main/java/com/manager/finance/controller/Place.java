package com.manager.finance.controller;

import com.manager.finance.config.LogConstants;
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
public class Place extends CrudApiResponse<PlaceModel> {
    private static final String PLACE = "place";
    private final LogConstants logConstants = new LogConstants(PLACE);
    private final PlaceModel placeModel;

    public Place(PlaceModel placeModel) {
        super(placeModel, PLACE);
        this.placeModel = placeModel;
    }

    @GetMapping
    public List<PlaceEntity> get() {
        List<PlaceEntity> places = placeModel.get();
        log.debug(logConstants.getListFiltered(), places);
        return places;
    }

    @GetMapping("{id}")
    public PlaceEntity get(@PathVariable("id") PlaceEntity place) {
        log.debug(logConstants.getInput(), place);
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
    public ResponseEntity<Object> deletePlace(@PathVariable("id") PlaceEntity placeEntity) {
        return delete(placeEntity);
    }

}

