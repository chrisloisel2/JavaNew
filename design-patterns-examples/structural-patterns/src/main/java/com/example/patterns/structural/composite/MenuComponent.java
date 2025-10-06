package com.example.patterns.structural.composite;

import java.util.List;

public sealed interface MenuComponent permits MenuItem, MenuGroup {
    String render();

    default List<MenuComponent> children() {
        return List.of();
    }
}
