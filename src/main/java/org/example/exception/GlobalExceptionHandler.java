package org.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.dto.GenericExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // TODO: Improve exception handling

    private static final String EXCEPTION_LOG_MESSAGE = "Global Exception Handler invoke: {}";

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<GenericExceptionResponse> handleNotFoundException(NotFoundException exception) {

        var dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info(EXCEPTION_LOG_MESSAGE, exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<GenericExceptionResponse> handleUnauthorizedException(UnauthorizedException exception) {

        var dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info(EXCEPTION_LOG_MESSAGE, exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericExceptionResponse> handleBadCredentialException(BadCredentialsException exception) {

        var dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info(EXCEPTION_LOG_MESSAGE, exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<GenericExceptionResponse> handleUserNotFoundException(UsernameNotFoundException exception) {

        var dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info(EXCEPTION_LOG_MESSAGE, exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

}
