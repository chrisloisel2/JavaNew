package com.app.catalog;

public record Book(String title, String author, int pages) implements Media {
}
