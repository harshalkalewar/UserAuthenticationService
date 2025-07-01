package com.example.userauthenticationservice.exceptions;

public class PasswordDoesNotMatchException extends Exception{
    public PasswordDoesNotMatchException(String message) {
        super(message);
    }
}
