package com.bike.web.exceptionhandling;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

  public record ErrorResponse(Instant timestamp, int status, String error, String message) {}

  @ExceptionHandler(value = {IllegalArgumentException.class})
  @ResponseStatus(BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<Object> handleException(IllegalArgumentException ex) {
    return new ResponseEntity<>(
        new ErrorResponse(
            Instant.now(), BAD_REQUEST.value(), BAD_REQUEST.getReasonPhrase(), ex.getMessage()),
        BAD_REQUEST);
  }

  @ExceptionHandler({EntityNotFoundException.class, EmptyResultDataAccessException.class})
  @ResponseStatus(NOT_FOUND)
  @ResponseBody
  public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
    return new ResponseEntity<>(
        new ErrorResponse(
            Instant.now(), NOT_FOUND.value(), NOT_FOUND.getReasonPhrase(), ex.getMessage()),
        NOT_FOUND);
  }
}
