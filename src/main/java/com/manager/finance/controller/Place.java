package com.manager.finance.controller;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.PlaceDTO;
import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.model.PlaceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/place")
@Slf4j
public class Place {
    private static final String PLACE = "place";
    private final LogConstants logConstants = new LogConstants(PLACE);
    private final PlaceModel placeModel;

    public Place(PlaceModel placeModel) {
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
    public ResponseEntity create(PlaceDTO placeDTO, BindingResult bindingResult) throws IOException {
        log.debug(logConstants.getInputDataNew(), placeDTO);
        ResponseEntity responseEntity;

        if (!bindingResult.hasErrors()) {
            PlaceEntity place = placeModel.create(placeDTO);
            log.debug(logConstants.getSaveToDatabase(), place);
            responseEntity = ResponseEntity.ok(place);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(logConstants.getErrorAdd(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(logConstants.getSavedResponse(), responseEntity);
        return responseEntity;
    }

    @PutMapping("{id}")
    public ResponseEntity change(@PathVariable("id") PlaceEntity placeEntity, @Valid PlaceDTO placeDTO,
                                 BindingResult bindingResult) {
        log.debug(logConstants.getInputDataToChange(), placeDTO, placeEntity);
        ResponseEntity responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(placeModel.change(placeEntity, placeDTO));
            log.debug(logConstants.getSaveToDatabase(), placeDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(logConstants.getErrorChange(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(logConstants.getUpdatedResponse(), responseEntity);
        return responseEntity;
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") PlaceEntity placeEntity) {
        log.debug(logConstants.getInputDataForDelete(), placeEntity);
        ResponseEntity responseEntity = ResponseEntity.ok(placeModel.delete(placeEntity));
        log.debug(logConstants.getDeletedResponse(), responseEntity);
        return responseEntity;
    }

}

