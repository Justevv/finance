package com.manager.finance.infrastructure.adapter.in.rest.error;

import com.manager.finance.domain.exception.EntityNotFoundException;
import com.manager.finance.infrastructure.adapter.in.exception.InvalidUUIDException;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.RestError;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.RestResponse;
import com.manager.user.exception.UserAlreadyExistException;
import com.manager.user.exception.UserIpAddressWasBlockedException;
import com.manager.user.exception.VerificationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity<Object> handleUserAlreadyExistException(
            UserAlreadyExistException ex, WebRequest request) {
        log.warn(ex.getMessage());
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleEntityNotFoundException(
            EntityNotFoundException ex, WebRequest request) {
        log.warn(ex.getMessage());
        var restError = RestError.builder()
                .text(ex.getMessage())
                .build();
        RestResponse<Object> response = new RestResponse<>(restError, null);
        return handleExceptionInternal(ex, response,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleInvalidUUIDException(
            InvalidUUIDException ex, WebRequest request) {
        log.warn(ex.getMessage());
        var restError = RestError.builder()
                .text(ex.getMessage())
                .build();
        RestResponse<Object> response = new RestResponse<>(restError, null);
        return handleExceptionInternal(ex, response,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleUserPrincipalNotFoundException(
            UsernameNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleVerificationNotFoundException(
            VerificationNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleUserIpAddressWasBlockedException(
            UserIpAddressWasBlockedException ex, WebRequest request) {
        log.warn(ex.getMessage());
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

}
