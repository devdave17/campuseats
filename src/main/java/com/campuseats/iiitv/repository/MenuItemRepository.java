package com.campuseats.iiitv.repository;

import com.campuseats.iiitv.model.MenuItem;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MenuItemRepository {

    private final Firestore firestore;
    private final CollectionReference collection;

    public MenuItemRepository(Firestore firestore) {
        this.firestore = firestore;
        this.collection = firestore.collection("menu-items");
    }

    public MenuItem save(MenuItem item) {

        try {
            collection.document(item.getId()).set(item).get();
            return item;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save menu item", e);
        }
    }

    public Optional<MenuItem> findById(String id) {

        try {
            DocumentSnapshot document =
                    collection.document(id).get().get();

            if (!document.exists()) {
                return Optional.empty();
            }

            MenuItem item = document.toObject(MenuItem.class);

            return Optional.ofNullable(item);

        } catch (Exception e) {
            throw new RuntimeException("Failed to find menu item", e);
        }
    }

    public List<MenuItem> findAll() {

        try {
            ApiFuture<QuerySnapshot> future =
                    collection.get();

            List<QueryDocumentSnapshot> documents =
                    future.get().getDocuments();

            List<MenuItem> items = new ArrayList<>();

            for (QueryDocumentSnapshot document : documents) {

                MenuItem item =
                        document.toObject(MenuItem.class);

                items.add(item);
            }

            return items;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch menu items", e);
        }
    }
}