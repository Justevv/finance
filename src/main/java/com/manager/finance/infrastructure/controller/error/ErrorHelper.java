package com.manager.finance.infrastructure.controller.error;

import com.manager.finance.metric.TrackExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ErrorHelper {
    private static final String ERROR_MESSAGE = " Error";

    @TrackExecutionTime
    public ResponseEntity<Object> checkErrors(BindingResult bindingResult) {
        ResponseEntity<Object> responseEntity = null;
        if (bindingResult.hasErrors()) {
            var errors = getErrors(bindingResult);
            log.debug("The errors to process entity {}: {}", bindingResult.getTarget(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    private Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + ERROR_MESSAGE, FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}

