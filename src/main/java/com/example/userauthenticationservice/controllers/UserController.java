package com.example.userauthenticationservice.controllers;

import com.example.userauthenticationservice.dtos.RoleDto;
import com.example.userauthenticationservice.dtos.UserDto;
import com.example.userauthenticationservice.exceptions.UserDoesNotExistException;
import com.example.userauthenticationservice.models.Role;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long id) throws UserDoesNotExistException {
        User user = userService.getUserById(id);
        if( user == null ) {
            throw new UserDoesNotExistException("User not found.");
        }

        return from(user);
    }

    private UserDto from(User user) {
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
