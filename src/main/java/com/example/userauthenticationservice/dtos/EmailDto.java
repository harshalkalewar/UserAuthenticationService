package com.example.userauthenticationservice.dtos;

import lombok.Data;

@Data
public class EmailDto {
    private String to;
    private String from;
    private String subject;
    private String body;
}
