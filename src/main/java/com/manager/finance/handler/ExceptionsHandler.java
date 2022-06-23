package com.manager.finance.handler;

import com.manager.finance.exception.PasswordResetTokenNotFoundException;
import com.manager.finance.exception.UserAlreadyExistException;
import com.manager.finance.exception.UserIpAddressWasBlockedException;
import com.manager.finance.exception.VerificationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.NonUniqueResultException;

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
    protected ResponseEntity<Object> handleUsernameNotFoundException(
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

    @ExceptionHandler
    protected ResponseEntity<Object> handleDatabaseError(
            NonUniqueResultException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        String bodyOfResponse = "The database is in a bad state. Please contact your system administrator.";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handlePasswordResetTokenNotFoundException(
            PasswordResetTokenNotFoundException ex, WebRequest request) {
        log.warn(ex.getMessage());
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
