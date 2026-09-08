package com.campuseats.iiitv.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MenuItem {

    private String id;
    private String name;
    private String description;
    private double price;
    private String category;
    private boolean available;
}