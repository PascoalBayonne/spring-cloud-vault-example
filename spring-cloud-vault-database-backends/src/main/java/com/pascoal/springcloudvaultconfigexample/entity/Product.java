package com.pascoal.springcloudvaultconfigexample.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class Product {
    @Id
    private String id;
    private Double price;
    private String description;

    public Product() {
        id = UUID.randomUUID().toString();
    }
}
