package com.manager.finance.helper;

import com.manager.finance.entity.CrudEntity;
import com.manager.finance.metric.TrackExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.security.Principal;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ErrorHelper {
    private static final String ERROR_MESSAGE = " Error";

    public ResponseEntity<Object> checkEntityNotBelongPrincipal(Principal principal, CrudEntity entity) {
        ResponseEntity<Object> responseEntity = null;
        if (!isEntityBelongPrincipal(principal, entity)) {
            responseEntity = new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

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

    private boolean isEntityBelongPrincipal(Principal principal, CrudEntity entity) {
        var username = principal.getName();
        var entityOwner = entity.getUser().getUsername();
        log.trace("Current username is {}, entity {} belongs {}", username, entity, entityOwner);
        return username.equals(entityOwner);
    }

    private Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + ERROR_MESSAGE, FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}

