package com.campuseats.iiitv.dto;

import com.campuseats.iiitv.model.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MenuItemResponse {

    private String id;
    private String name;
    private String description;
    private double price;
    private String category;
    private boolean available;
    private long version;

    public MenuItemResponse(MenuItem item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.category = item.getCategory();
        this.available = item.isAvailable();
        this.version = item.getVersion();
    }
}