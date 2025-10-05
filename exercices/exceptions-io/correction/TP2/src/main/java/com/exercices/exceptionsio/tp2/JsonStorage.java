package com.exercices.exceptionsio.tp2;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class JsonStorage implements CatalogStorage {

    private final HttpClient client;
    private final Path fichierLocal;
    private final URI endpoint;

    public JsonStorage(Path fichierLocal, URI endpoint) {
        this(HttpClient.newHttpClient(), fichierLocal, endpoint);
    }

    public JsonStorage(HttpClient client, Path fichierLocal, URI endpoint) {
        this.client = client;
        this.fichierLocal = fichierLocal;
        this.endpoint = endpoint;
    }

    @Override
    public void sauvegarder(List<Emprunt> emprunts) throws StorageException {
        String json = emprunts.stream()
                .map(this::toJson)
                .collect(Collectors.joining(",", "[", "]"));
        try {
            Files.writeString(fichierLocal, json);
            envoyerVersRemote(json);
        } catch (IOException e) {
            throw new StorageException("Erreur d'écriture locale", e);
        }
    }

    private void envoyerVersRemote(String json) throws RemoteStorageException {
        HttpRequest request = HttpRequest.newBuilder(endpoint)
                .timeout(Duration.ofSeconds(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        AtomicInteger tentatives = new AtomicInteger();
        while (tentatives.getAndIncrement() < 3) {
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    System.out.println("Envoi distant simulé : " + response.body());
                    return;
                }
                throw new RemoteStorageException("Statut HTTP inattendu : " + response.statusCode());
            } catch (IOException | InterruptedException e) {
                if (tentatives.get() >= 3) {
                    throw new RemoteStorageException("Echec d'envoi après retries", e);
                }
                try {
                    Thread.sleep(250L * tentatives.get());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new RemoteStorageException("Thread interrompu", ex);
                }
            }
        }
    }

    @Override
    public List<Emprunt> charger() throws StorageException {
        if (!Files.exists(fichierLocal)) {
            return List.of();
        }
        try {
            String json = Files.readString(fichierLocal);
            return JsonSupport.fromJson(json);
        } catch (IOException e) {
            throw new StorageException("Lecture JSON impossible", e);
        }
    }

    private String toJson(Emprunt emprunt) {
        return "{" + "\"isbn\":\"" + emprunt.livre().isbn() + "\"," +
                "\"titre\":\"" + emprunt.livre().titre() + "\"," +
                "\"auteur\":\"" + emprunt.livre().auteur() + "\"," +
                "\"parution\":\"" + emprunt.livre().parution() + "\"," +
                "\"debut\":\"" + emprunt.debut() + "\"," +
                "\"fin\":\"" + emprunt.fin() + "\"" +
                "}";
    }
}
