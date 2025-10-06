package com.example.java17;

/**
 * Les messages NPE détaillés aident à diagnostiquer la variable fautive.
 */
public class NullPointerDemo {

    public void trigger() {
        String text = null;
        // En Java 17, l'exception indiquera que la variable "text" est nulle.
        text.length();
    }
}
