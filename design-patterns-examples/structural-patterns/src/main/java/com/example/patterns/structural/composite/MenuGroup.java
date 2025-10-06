package com.example.patterns.structural.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MenuGroup implements MenuComponent {

    private final String title;
    private final List<MenuComponent> children = new ArrayList<>();

    public MenuGroup(String title) {
        this.title = title;
    }

    public MenuGroup add(MenuComponent component) {
        children.add(component);
        return this;
    }

    @Override
    public String render() {
        StringBuilder builder = new StringBuilder(title).append('\n');
        children.forEach(child -> builder.append("  ").append(child.render()).append('\n'));
        return builder.toString();
    }

    @Override
    public List<MenuComponent> children() {
        return Collections.unmodifiableList(children);
    }
}
