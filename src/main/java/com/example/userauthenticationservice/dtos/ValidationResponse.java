package com.example.userauthenticationservice.dtos;

import lombok.Data;

@Data
public class ValidationResponse {
    private boolean response;

    public ValidationResponse(boolean valid) {
        this.response = valid;
    }
}
