package com.campuseats.iiitv.controller;

import com.campuseats.iiitv.dto.AvailabilityRequest;
import com.campuseats.iiitv.dto.CreateMenuItemRequest;
import com.campuseats.iiitv.dto.MenuItemResponse;
import com.campuseats.iiitv.service.MenuItemService;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping(value = "/menu-items", produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuItemController {

    private final MenuItemService service;

    public MenuItemController(MenuItemService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
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
            @PathVariable String id) {

        MenuItemResponse response = service.getById(id);
        String etag = generateEtag(response);

        return ResponseEntity.ok()
                .cacheControl(
                        CacheControl.maxAge(Duration.ofSeconds(60))
                                .cachePublic()
                                .mustRevalidate()
                )
                .eTag(etag)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> list(
            @RequestParam(required = false) String category) {

        return ResponseEntity.ok(service.findByCategory(category));
    }

    @PatchMapping(
            value = "/{id}/availability",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MenuItemResponse> availability(
            @PathVariable String id,
            @RequestBody AvailabilityRequest request) {

        return ResponseEntity.ok(
                service.updateAvailability(id, request));
    }

    private String generateEtag(MenuItemResponse response) {
        String representation =
                nullSafe(response.getId()) + "\u0000" +
                        nullSafe(response.getName()) + "\u0000" +
                        nullSafe(response.getDescription()) + "\u0000" +
                        Double.toString(response.getPrice()) + "\u0000" +
                        nullSafe(response.getCategory()) + "\u0000" +
                        Boolean.toString(response.isAvailable());

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash =
                    digest.digest(representation.getBytes(StandardCharsets.UTF_8));

            // ETag values are quoted strings in HTTP.
            return "\"" + Base64.getUrlEncoder().withoutPadding().encodeToString(hash) + "\"";
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }
}
