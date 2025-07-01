package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.exceptions.PasswordDoesNotMatchException;
import com.example.userauthenticationservice.exceptions.SessionNotFoundException;
import com.example.userauthenticationservice.exceptions.UserAlreadyExistsException;
import com.example.userauthenticationservice.exceptions.UserDoesNotExistException;
import com.example.userauthenticationservice.models.Role;
import com.example.userauthenticationservice.models.User;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;

public interface IAuthenticationService {

    User signUp(String email, String password, List<Role> roles) throws UserAlreadyExistsException;

    Pair<User, String> login(String email, String password) throws UserDoesNotExistException, PasswordDoesNotMatchException;

    boolean validateToken(String token, Long userId) throws SessionNotFoundException;
}
