package com.example.userauthenticationservice.models;


import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Role extends BaseModel{
    private String value;
}
