package com.example.patterns.creational.abstractfactory;

/**
 * Abstract Factory pour créer des composants cohérents (clair ou sombre).
 */
public interface ThemeFactory {
    Button createButton();
    Toolbar createToolbar();

    static ThemeFactory dark() {
        return new DarkThemeFactory();
    }

    static ThemeFactory light() {
        return new LightThemeFactory();
    }

    interface Button {
        String render();
    }

    interface Toolbar {
        String render();
    }

    final class DarkThemeFactory implements ThemeFactory {
        @Override
        public Button createButton() {
            return () -> "[Dark Button]";
        }

        @Override
        public Toolbar createToolbar() {
            return () -> "[Dark Toolbar]";
        }
    }

    final class LightThemeFactory implements ThemeFactory {
        @Override
        public Button createButton() {
            return () -> "[Light Button]";
        }

        @Override
        public Toolbar createToolbar() {
            return () -> "[Light Toolbar]";
        }
    }
}
