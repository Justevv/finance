package com.manager.finance.handler;

import com.manager.finance.exception.UserAlreadyExistException;
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

    @ExceptionHandler(UserAlreadyExistException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistException(
            UserAlreadyExistException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleUserPrincipalNotFoundException(
            UsernameNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
