package com.demo.actor.advices;

import com.mongodb.MongoQueryException;
import com.mongodb.MongoWriteException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ActorAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {NotFoundException.class})
  protected ResponseEntity<Object> handleNotFoundException(RuntimeException ex,
      WebRequest webRequest) {
    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND,
        webRequest);
  }

  @ExceptionHandler(value = {MongoQueryException.class})
  protected ResponseEntity<Object> handleMongoQueryException(RuntimeException ex,
      WebRequest webRequest) {
    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
        webRequest);
  }

  @ExceptionHandler(value = {MongoWriteException.class})
  protected ResponseEntity<Object> handleMongoWriteException(RuntimeException ex,
      WebRequest webRequest) {
    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
        webRequest);
  }
}
