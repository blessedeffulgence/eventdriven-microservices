package com.eventdriven.microservices.elastic.query.service.errorhandler;

import com.eventdriven.microservices.elastic.query.service.api.ElasticDocumentController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ElasticServiceErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticServiceErrorHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handle(AccessDeniedException accessDeniedException)
    {
        LOG.error("Access denied",accessDeniedException);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access this resource");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handle(IllegalArgumentException exception)
    {
        LOG.error("Illegal argument exception",exception);
        return ResponseEntity.badRequest().body("Illegal argument exception" + exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException exception)
    {
        LOG.error("Service runtime exception",exception);
        return ResponseEntity.badRequest().body("Service runtime exception" + exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception exception)
    {
        LOG.error("Internal Server error",exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A server error occurred");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handle(MethodArgumentNotValidException exception)
    {
        LOG.error("Method argument validation exception",exception);
        Map<String,String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors()
                .forEach(error -> errors.put(((FieldError)error).getField(),error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
