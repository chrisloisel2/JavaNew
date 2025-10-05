package com.app.catalog;

public record AudioBook(String title, String author, int durationMinutes, String narrator) implements Media {
}
