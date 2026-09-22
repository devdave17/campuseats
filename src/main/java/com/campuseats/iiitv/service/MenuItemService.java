package com.campuseats.iiitv.service;

import com.campuseats.iiitv.dto.AvailabilityRequest;
import com.campuseats.iiitv.dto.CreateMenuItemRequest;
import com.campuseats.iiitv.dto.MenuItemResponse;
import com.campuseats.iiitv.exception.MenuItemNotFoundException;
import com.campuseats.iiitv.model.MenuItem;
import com.campuseats.iiitv.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MenuItemService {

    private final MenuItemRepository repository;

    private final ConcurrentHashMap<String, MenuItemResponse> idempotencyStore =
            new ConcurrentHashMap<>();

    public MenuItemService(MenuItemRepository repository) {
        this.repository = repository;
    }

    public MenuItemResponse create(CreateMenuItemRequest request,
                                   String idempotencyKey) {

        // Existing Part A/C idempotency behavior is preserved.
        MenuItemResponse existing = idempotencyStore.get(idempotencyKey);

        if (existing != null) {
            return existing;
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (request.getCategory() == null || request.getCategory().isBlank()) {
            throw new IllegalArgumentException("Category is required");
        }

        if (request.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        MenuItem item = new MenuItem(
                UUID.randomUUID().toString(),
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getCategory(),
                true
        );

        repository.save(item);

        MenuItemResponse response = new MenuItemResponse(item);
        idempotencyStore.put(idempotencyKey, response);

        return response;
    }

    public MenuItemResponse getById(String id) {
        return repository.findById(id)
                .map(MenuItemResponse::new)
                .orElseThrow(() ->
                        new MenuItemNotFoundException(id));
    }

    public List<MenuItemResponse> findByCategory(String category) {

        return repository.findAll()
                .stream()
                .filter(item ->
                        category == null ||
                                item.getCategory().equalsIgnoreCase(category))
                .map(MenuItemResponse::new)
                .collect(Collectors.toList());
    }

    public MenuItemResponse updateAvailability(
            String id,
            AvailabilityRequest request) {

        MenuItem item = repository.findById(id)
                .orElseThrow(() ->
                        new MenuItemNotFoundException(id));

        item.setAvailable(request.isAvailable());
        repository.save(item);

        return new MenuItemResponse(item);
    }
}
