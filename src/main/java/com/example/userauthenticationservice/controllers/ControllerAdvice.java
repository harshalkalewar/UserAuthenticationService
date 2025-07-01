package com.example.userauthenticationservice.controllers;

import com.example.userauthenticationservice.exceptions.PasswordDoesNotMatchException;
import com.example.userauthenticationservice.exceptions.SessionNotFoundException;
import com.example.userauthenticationservice.exceptions.UserAlreadyExistsException;
import com.example.userauthenticationservice.exceptions.UserDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<String> handleUserDoesNotExistException(UserDoesNotExistException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordDoesNotMatchException.class)
    public ResponseEntity<String> handlePasswordDoesNotMatchException(PasswordDoesNotMatchException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<String> handleSessionNotFoundException(SessionNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}

