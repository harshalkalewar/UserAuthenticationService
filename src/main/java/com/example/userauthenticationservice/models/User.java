package com.example.userauthenticationservice.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class User extends BaseModel{

    private String email;

    private String password;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

}
