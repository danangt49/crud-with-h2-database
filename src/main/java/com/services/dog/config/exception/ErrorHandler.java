package com.services.dog.config.exception;

import com.services.dog.config.GlobalApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFound(NoHandlerFoundException ex) {
        return new GlobalApiResponse<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex) {
        log.error("======================================================>>");
        log.error("Exception message=>{}", ex.getMessage());
        return new GlobalApiResponse<>(ex.getMessage(), ex.getHttpStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        return new GlobalApiResponse<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        log.error("Bad=>{}", ex.getMessage());
//        return new GlobalApiResponse<>("Please contact admin", HttpStatus.INTERNAL_SERVER_ERROR);
        return new GlobalApiResponse<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException ex) {
        String message = "";
        Map<String, List<String>> map = new HashMap<>();
        for (final var error : ex.getBindingResult().getFieldErrors()) {
            List<String> msgs = new ArrayList<>();
            if (map.containsKey(error.getField())) msgs = map.get(error.getField());
            msgs.add(error.getDefaultMessage());
            map.put(error.getField(), msgs);
            message = error.getDefaultMessage();
        }

        for (final var error : ex.getBindingResult().getGlobalErrors()) {
            List<String> msgs = new ArrayList<>();
            if (map.containsKey(error.getObjectName())) msgs = map.get(error.getObjectName());
            msgs.add(error.getDefaultMessage());
            map.put(error.getObjectName(), msgs);
            message = error.getDefaultMessage();
        }
        return new GlobalApiResponse<>(map, message, HttpStatus.BAD_REQUEST);
    }
}
