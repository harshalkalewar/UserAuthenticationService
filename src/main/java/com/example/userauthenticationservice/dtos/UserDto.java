package com.example.userauthenticationservice.dtos;

import com.example.userauthenticationservice.models.Role;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String email;
    private List<RoleDto> roles = new ArrayList<>();
}
