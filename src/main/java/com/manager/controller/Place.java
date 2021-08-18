package com.manager.controller;

import com.manager.config.LogConstants;
import com.manager.dto.PlaceDTO;
import com.manager.entity.PlaceEntity;
import com.manager.model.PlaceModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class Place {
    private static final Logger LOGGER = LogManager.getLogger(Place.class);
    private static final String PLACE = "place";
    private final LogConstants logConstants = new LogConstants(PLACE);
    private final PlaceModel placeModel;

    public Place(PlaceModel placeModel) {
        this.placeModel = placeModel;
    }

    @GetMapping
    public List<PlaceEntity> get() {
        List<PlaceEntity> places = placeModel.get();
        LOGGER.debug(logConstants.getListFiltered(), places);
        return places;
    }

    @GetMapping("{id}")
    public PlaceEntity get(@PathVariable("id") PlaceEntity place) {
        LOGGER.debug(logConstants.getInput(), place);
        return place;
    }

    @PostMapping
    public ResponseEntity create(PlaceDTO placeDTO, BindingResult bindingResult) throws IOException {
        LOGGER.debug(logConstants.getInputDataNew(), placeDTO);
        ResponseEntity responseEntity;

        if (!bindingResult.hasErrors()) {
            PlaceEntity place = placeModel.create(placeDTO);
            LOGGER.debug(logConstants.getSaveToDatabase(), place);
            responseEntity = ResponseEntity.ok(place);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            LOGGER.debug(logConstants.getErrorAdd(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        LOGGER.debug(logConstants.getSavedResponse(), responseEntity);
        return responseEntity;
    }

    @PutMapping("{id}")
    public ResponseEntity change(@PathVariable("id") PlaceEntity placeEntity, @Valid PlaceDTO placeDTO,
                                 BindingResult bindingResult) {
        LOGGER.debug(logConstants.getInputDataToChange(), placeDTO, placeEntity);
        ResponseEntity responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(placeModel.change(placeEntity, placeDTO));
            LOGGER.debug(logConstants.getSaveToDatabase(), placeDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            LOGGER.debug(logConstants.getErrorChange(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        LOGGER.debug(logConstants.getUpdatedResponse(), responseEntity);
        return responseEntity;
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") PlaceEntity placeEntity) {
        LOGGER.debug(logConstants.getInputDataForDelete(), placeEntity);
        ResponseEntity responseEntity = ResponseEntity.ok(placeModel.delete(placeEntity));
        LOGGER.debug(logConstants.getDeletedResponse(), responseEntity);
        return responseEntity;
    }

}

