package com.example.userauthenticationservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Session extends BaseModel {
    @Column(length = 1000)
    private String token;

    @ManyToOne
    private User user;
}
