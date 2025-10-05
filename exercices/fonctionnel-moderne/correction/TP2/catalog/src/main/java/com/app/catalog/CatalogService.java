package com.app.catalog;

import java.util.List;

public final class CatalogService {

    private final List<Media> medias = List.of(
            new Book("Design Patterns", "GoF", 395),
            new AudioBook("Clean Code", "Robert C. Martin", 750, "Narrateur Studio"),
            new Book("Effective Java", "Joshua Bloch", 416)
    );

    public List<Media> listAll() {
        return medias;
    }
}
