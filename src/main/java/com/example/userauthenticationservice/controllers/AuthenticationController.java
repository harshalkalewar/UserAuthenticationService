package com.example.userauthenticationservice.controllers;

import com.example.userauthenticationservice.dtos.*;
import com.example.userauthenticationservice.exceptions.PasswordDoesNotMatchException;
import com.example.userauthenticationservice.exceptions.SessionNotFoundException;
import com.example.userauthenticationservice.exceptions.UserAlreadyExistsException;
import com.example.userauthenticationservice.exceptions.UserDoesNotExistException;
import com.example.userauthenticationservice.models.Role;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.services.IAuthenticationService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private IAuthenticationService authenticationService;

//    @Autowired
//    private IAuthenticationService iAuthenticationService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignupRequestDto signupRequestDto) throws UserAlreadyExistsException {
        try {
            User user = authenticationService.signUp(signupRequestDto.getEmail(), signupRequestDto.getPassword(), signupRequestDto.getRoles());
            return new ResponseEntity<>(from(user), HttpStatus.CREATED);

        } catch (UserAlreadyExistsException e) {
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) throws PasswordDoesNotMatchException, UserDoesNotExistException {
        try{
            Pair<User, String> response = authenticationService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

            ResponseCookie cookie = ResponseCookie.from("jwt", response.b)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(3600)
                    .build();

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            return new ResponseEntity<>(from(response.a), headers, HttpStatus.OK);

        } catch (UserDoesNotExistException exception) {
            throw exception;
        } catch (PasswordDoesNotMatchException exception) {
            throw exception;
        }
    }


    @PostMapping("/validate")
    public ResponseEntity<ValidationResponse> validateToken(@RequestBody ValidateTokenDto validateTokenDto) throws SessionNotFoundException {
        boolean response = authenticationService.validateToken(validateTokenDto.getToken(), validateTokenDto.getUserId());
        if(!response){
            throw new SessionNotFoundException("invalid token.");
        }
        return new ResponseEntity<>(new ValidationResponse(response), HttpStatus.OK);
    }


    private UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        if(user.getRoles() != null ) {
            List<RoleDto> roles = new ArrayList<>();
            for (Role role : user.getRoles()) {
                roles.add(from(role));
            }
            userDto.setRoles(roles);
        } else{
            userDto.setRoles(new ArrayList<>());
        }
        return userDto;
    }

    private RoleDto from(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setRoleName(role.getValue());
        return roleDto;
    }

}
