package com.example.java11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simplifie la lecture/écriture grâce aux nouvelles méthodes statiques de {@link Files}.
 */
public class FileApi {

    public void writeMessage(Path path, String message) throws IOException {
        Files.writeString(path, message);
    }

    public String readMessage(Path path) throws IOException {
        return Files.readString(path);
    }
}
