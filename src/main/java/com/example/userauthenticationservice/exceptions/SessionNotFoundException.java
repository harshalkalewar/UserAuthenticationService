package com.example.userauthenticationservice.exceptions;

public class SessionNotFoundException extends Exception{
    public SessionNotFoundException(String message){
        super(message);
    }
}
