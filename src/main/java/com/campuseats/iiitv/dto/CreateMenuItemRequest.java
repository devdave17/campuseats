package com.campuseats.iiitv.dto;

import lombok.Data;

@Data
public class CreateMenuItemRequest {

    private String name;
    private String description;
    private double price;
    private String category;
}