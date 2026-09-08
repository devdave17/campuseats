package com.campuseats.iiitv.repository;

import com.campuseats.iiitv.model.MenuItem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MenuItemRepository {

    private final Map<String, MenuItem> items = new ConcurrentHashMap<>();

    public MenuItem save(MenuItem item) {
        items.put(item.getId(), item);
        return item;
    }

    public Optional<MenuItem> findById(String id) {
        return Optional.ofNullable(items.get(id));
    }

    public List<MenuItem> findAll() {
        return new ArrayList<>(items.values());
    }
}