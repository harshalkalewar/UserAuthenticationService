package com.example.userauthenticationservice.dtos;

import lombok.Data;

@Data
public class ValidateTokenDto {

    private String token;
    private Long userId;
}
