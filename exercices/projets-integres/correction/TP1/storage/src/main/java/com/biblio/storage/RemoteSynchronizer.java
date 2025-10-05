package com.biblio.storage;

import com.biblio.core.Livre;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public final class RemoteSynchronizer {

    private final HttpClient client;
    private final URI endpoint;

    public RemoteSynchronizer(HttpClient client, URI endpoint) {
        this.client = client;
        this.endpoint = endpoint;
    }

    public void envoyer(List<Livre> livres) throws StorageException {
        String body = livres.stream()
                .map(Livre::titre)
                .reduce("", (acc, titre) -> acc + titre + "\n");
        HttpRequest request = HttpRequest.newBuilder(endpoint)
                .timeout(Duration.ofSeconds(2))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new StorageException("Synchronisation distante échouée : " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StorageException("Erreur lors de l'appel distant", e);
        }
    }
}
