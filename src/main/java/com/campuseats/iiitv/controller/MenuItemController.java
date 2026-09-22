package com.campuseats.iiitv.controller;

import com.campuseats.iiitv.dto.AvailabilityRequest;
import com.campuseats.iiitv.dto.CreateMenuItemRequest;
import com.campuseats.iiitv.dto.MenuItemResponse;
import com.campuseats.iiitv.service.MenuItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {

    private final MenuItemService service;

    public MenuItemController(MenuItemService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MenuItemResponse> create(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody CreateMenuItemRequest request) {

        MenuItemResponse response =
                service.create(request, idempotencyKey);

        return ResponseEntity
                .created(URI.create("/menu-items/" + response.getId()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> get(
            @PathVariable String id,
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

        MenuItemResponse response = service.getById(id);

        String etag = "\"" + response.getVersion() + "\"";

        if (etag.equals(ifNoneMatch)) {
            return ResponseEntity
                    .status(304)
                    .eTag(etag)
                    .build();
        }

        return ResponseEntity
                .ok()
                .eTag(etag)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> list(
            @RequestParam(required = false) String category) {

        return ResponseEntity.ok(service.findByCategory(category));
    }

    @PostMapping("/{id}/availability")
    public ResponseEntity<MenuItemResponse> availability(
            @PathVariable String id,
            @RequestHeader(value = "If-Match", required = false) String ifMatch,
            @RequestBody AvailabilityRequest request) {

        MenuItemResponse response =
                service.updateAvailability(id, request, ifMatch);

        String etag = "\"" + response.getVersion() + "\"";

        return ResponseEntity
                .ok()
                .eTag(etag)
                .body(response);
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> options() {

        return ResponseEntity
                .ok()
                .header("Allow", "GET, POST, OPTIONS")
                .build();
    }
}