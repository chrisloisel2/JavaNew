package com.example.java9.module;

/**
 * Exemple trivial montrant comment une classe publique est exportée depuis le module
 * {@code com.example.java9}. Un consommateur devra déclarer {@code requires com.example.java9}
 * dans son propre module-info.java.
 */
public class ModuleGreeting {
    public String greet(String name) {
        return "Bonjour, " + name + " !";
    }
}
