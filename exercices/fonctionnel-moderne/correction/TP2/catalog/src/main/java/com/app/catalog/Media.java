package com.app.catalog;

public sealed interface Media permits Book, AudioBook {
    String title();
    String author();
}
