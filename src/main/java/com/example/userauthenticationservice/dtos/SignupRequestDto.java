package com.example.userauthenticationservice.dtos;

import com.example.userauthenticationservice.models.Role;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SignupRequestDto {

    private String email;
    private String password;
    private List<Role> roles;
}
